package utility;

import controller.Controller;
import model.*;

import javax.swing.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static utility.GameInfo.*;

public class ChangeListener {

    private final Controller controller;
    private final ConcurrentHashMap<Path, GameID> monitoredPaths = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Path, GameID> pendingChanges = new ConcurrentHashMap<>();
    private final JPanel mainPanel;
    private final ResourceBundle labels;
    private volatile boolean paused = false;
    private WatchService watchService = null;

    public ChangeListener(Controller controller, JPanel mainPanel, ResourceBundle labels) {
        this.controller = controller;
        this.mainPanel = mainPanel;
        this.labels = labels;

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (Exception e) {
            ExceptionHandler.getInstance().handleException("ChangeListener", e);
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        processPendingChanges();
    }

    private void processPendingChanges() {
        for (Map.Entry<Path, GameID> entry : pendingChanges.entrySet())
            handleFileChange(entry.getKey(), entry.getValue());
        pendingChanges.clear();
    }

    public CompletableFuture<Void> poll() {
        CompletableFuture<Void> display = CompletableFuture.runAsync(this::pollDisplay);
        CompletableFuture<Void> games = CompletableFuture.runAsync(this::pollGames);
        return CompletableFuture.allOf(display, games);
    }

    private void pollDisplay() {
        // Previous
        Display previousDisplay = controller.getDisplay();

        // Current
        Display currentDisplay = new Display();
        currentDisplay.setRecommendedRes(WindowsDisplayInfo.retrieveAdjustedResolution());
        currentDisplay.setAllResolutions(WindowsDisplayInfo.retrieveAllResolutions(false));

        // Handle Changes
        if (!currentDisplay.equals(previousDisplay)) {
            System.out.println("NEW DISPLAY: " + previousDisplay.getRecommendedRes() + " > " + currentDisplay.getRecommendedRes());
            controller.updateDisplay(currentDisplay);
            controller.updateView();
        }
    }

    private void pollGames() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Map<String, Boolean> runningStatus = retrieveGamesRunningStatus();

        for (Game game : controller.getGames()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> pollSingleGame(game, runningStatus));
            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();
    }

    private void pollSingleGame(Game game, Map<String, Boolean> runningStatus) {
        // Previous & Current
        String regPath = retrieveRegistryPath(game.getId());
        Path previousInstallPath = game.getInstallationPath();
        Path currentInstallPath = retrieveInstallPath(regPath);
        Version previousVersion = game.getVersionInstalled();
        Version currentVersion = retrieveVersionInstalled(regPath);
        boolean previousIsRunning = game.isRunning();
        boolean currentIsRunning = retrieveGameRunningFlag(game, runningStatus);

        // Handle InstallPath
        if (areDifferent(currentInstallPath, previousInstallPath)) {
            Path previousUserDataPath = game.getUserDataPath();
            Path currentUserDataPath = retrieveUserDataPath(regPath);

            game.setInstallationPath(currentInstallPath);
            game.setLanguage(retrieveLanguage(regPath));
            game.setUserDataPath(currentUserDataPath);
            game.setInGameResolution(retrieveResolution(Paths.get(currentUserDataPath + "/Options.ini")));

            Maps maps = new Maps();
            maps.setFilePath(Paths.get(currentInstallPath + "/Maps.big"));
            maps.setBackupPath(Paths.get(currentInstallPath + "/Maps_Backup.big"));
            maps.setHash(retrieveMapsBigHash(maps.getFilePath()));
            maps.setAspectRatio(retrieveMapsAspectRatio(maps.getHash()));
            maps.setBackup(Files.exists(maps.getBackupPath()));
            game.setMaps(maps);

            HUD hud = new HUD();
            hud.setFilePath(Paths.get(currentInstallPath + "/_unstretched-hud.big"));
            hud.setOriginal(Files.notExists(hud.getFilePath()));
            game.setHud(hud);

            if (previousInstallPath != null) {
                unregisterListener(previousInstallPath);
                unregisterListener(previousUserDataPath);
                System.out.println("NEW " + game.getId() + ": Registered: " + monitoredPaths);
            }
            if (currentInstallPath != null) {
                registerListener(currentInstallPath, game.getId());
                registerListener(currentUserDataPath, game.getId());
                System.out.println("NEW " + game.getId() + ": Registered: " + monitoredPaths);
            }

            controller.updateGameView(game.getId());
        }

        // Handle Version
        if (areDifferent(currentVersion, previousVersion)) {
            System.out.println("NEW " + game.getId() + ": Version: " + previousVersion + " > " + currentVersion);
            game.setVersionInstalled(currentVersion);

            if (game.getVersionInstalled() == null) {
                game.setInstalled(false);
                game.setPatched(false);
            }
            if (game.getVersionInstalled() != null) {
                game.setInstalled(game.getVersionInstalled().getNumber() > 0);
                game.setPatched(game.getVersionInstalled().getNumber() >= game.getVersionAvailablePatch().getNumber());
            }

            controller.updateGameView(game.getId());
        }

        // Handle Running
        if (areDifferent(currentIsRunning, previousIsRunning)) {
            System.out.println("NEW " + game.getId() + ": Running: " + previousIsRunning + " > " + currentIsRunning);
            game.setRunning(currentIsRunning);

            controller.updateGameView(game.getId());
        }
    }

    public void registerListener(Path path, GameID gameId) {
        try {
            if (Files.notExists(path))
                Files.createDirectories(path);

            monitoredPaths.put(path, gameId);

            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
        } catch (Exception e) {
            ExceptionHandler.getInstance().handleException("registerListener", e);
        }
    }

    public void unregisterListener(Path path) {
        monitoredPaths.remove(path);
    }

    public void startListening() {
        CompletableFuture.runAsync(() -> {
            try {
                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue; // Skip overflow events
                        }

                        Path context = (Path) event.context();
                        Path directory = (Path) key.watchable();

                        // Get the associated GameID directly from the monitoredPaths
                        GameID gameId = monitoredPaths.get(directory);

                        // Notify listeners about the file change event
                        handleFileChange(directory.resolve(context), gameId);
                    }
                    key.reset();
                }
            } catch (Exception e) {
                ExceptionHandler.getInstance().handleException("startListening", e);
            }
        });
    }

    private void handleFileChange(Path filePath, GameID gameId) {
        if (filePath == null)
            return;

        Game game = controller.getGame(gameId);
        if (game == null)
            return;

        if (paused) {
            pendingChanges.put(filePath, gameId);
            return;
        }

        // Handle File changes
        if (filePath.toString().contains("Options.ini")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            game.setInGameResolution(retrieveResolution(filePath));

            controller.updateGameView(game.getId());

            resolutionInvalidFeedback(game);
            resolutionSoftFeedback(game);

        } else if (filePath.toString().contains("Maps.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            Maps maps = game.getMaps();
            maps.setHash(retrieveMapsBigHash(filePath));
            maps.setAspectRatio(retrieveMapsAspectRatio(maps.getHash()));
            game.setMaps(maps);

            controller.updateGameView(game.getId());

        } else if (filePath.toString().contains("Maps_Backup.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            Maps maps = game.getMaps();
            maps.setBackup(Files.exists(filePath));
            game.setMaps(maps);

            controller.updateGameView(game.getId());

        } else if (filePath.toString().contains("_unstretched-hud.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            HUD hud = game.getHud();
            hud.setOriginal(Files.notExists(filePath));
            game.setHud(hud);

            controller.updateGameView(game.getId());
        }
    }

    public void resolutionInvalidFeedback(Game game) {
        GameID gameID = game.getId();
        if (isInvalidResolution(game, controller.getDisplay()))
            JOptionPane.showMessageDialog(mainPanel, labels.getString("app") + " " + (gameID.ordinal()+1) + ": " +
                    String.format(labels.getString("feedback.resolution.notSupported"),
                    controller.getGames().get(gameID.ordinal()).getInGameResolution()));
    }

    private void resolutionSoftFeedback(Game game) {
        if (game.isRunning() || !game.isInstalled() || game.getInGameResolution() == null)
            return;
        
        // Resolution Too High
        if (game.getInGameResolution().getWidth() > controller.getDisplay().getRecommendedRes().getWidth() ||
            game.getInGameResolution().getHeight() > controller.getDisplay().getRecommendedRes().getHeight())
            JOptionPane.showMessageDialog(mainPanel, labels.getString("app") + " " + (game.getId().ordinal()+1) + ": " +
                    labels.getString("feedback.resolution.tooHigh"));

        // Resolution 32:9
        if (game.getInGameResolution().getAspectRatio().equals("32:9"))
            JOptionPane.showMessageDialog(mainPanel, labels.getString("app") + " " + (game.getId().ordinal()+1) + ": " +
                    labels.getString("feedback.resolution.32_9"));
    }

    public boolean areDifferent(Object current, Object previous) {
        return (current != null && previous != null && !current.equals(previous)) ||
                (current != null && previous == null) || (current == null && previous != null);
    }
}
