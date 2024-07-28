package listener;

import controller.Controller;
import helper.ExceptionHandler;
import helper.WindowsDisplayInfo;
import model.*;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import static helper.GameInfo.*;

public class ChangeListener {

    private final Controller controller;
    private final ConcurrentHashMap<Path, GameID> monitoredPaths = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Path, GameID> pendingChanges = new ConcurrentHashMap<>();
    private volatile boolean paused = false;
    private WatchService watchService = null;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Map<Path, ScheduledFuture<?>> debounceMap = new ConcurrentHashMap<>();
    private final Map<Path, Long> lastEventTimestamps = new ConcurrentHashMap<>();
    private static final long DEBOUNCE_DELAY_MS = 100; // Adjust the delay as necessary
    private static final long MIN_EVENT_INTERVAL_MS = 100; // Adjust the interval as necessary

    public ChangeListener(Controller controller) {
        this.controller = controller;

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
            controller.updateAllResBoxes();
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

    public void listenToFilesystem() {
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
                        Path fullPath = directory.resolve(context);

                        long now = System.currentTimeMillis();
                        long lastEventTime = lastEventTimestamps.getOrDefault(fullPath, 0L);

                        if (now - lastEventTime > MIN_EVENT_INTERVAL_MS) {
                            lastEventTimestamps.put(fullPath, now);
                            debounceEvent(fullPath);
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                ExceptionHandler.getInstance().handleException("startListening", e);
            }
        });
    }

    private void debounceEvent(Path path) {
        ScheduledFuture<?> existingTask = debounceMap.get(path);
        if (existingTask != null) {
            existingTask.cancel(false);
        }

        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
            GameID gameId = monitoredPaths.get(path.getParent());
            handleFileChange(path, gameId);
            debounceMap.remove(path);
        }, DEBOUNCE_DELAY_MS, TimeUnit.MILLISECONDS);

        debounceMap.put(path, newTask);
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

            controller.updateResBox(game.getId());
            controller.resolutionInvalidFeedback(game);
            controller.resolutionTooHighFeedback(game);
            controller.resolution32_9Feedback(game);

        } else if (filePath.toString().contains("Maps.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            Maps maps = game.getMaps();
            maps.setHash(retrieveMapsBigHash(filePath));
            maps.setAspectRatio(retrieveMapsAspectRatio(maps.getHash()));
            game.setMaps(maps);

            controller.updateMapsBox(game.getId());

        } else if (filePath.toString().contains("Maps_Backup.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            Maps maps = game.getMaps();
            maps.setBackup(Files.exists(filePath));
            game.setMaps(maps);

            controller.updateMapsBox(game.getId());

        } else if (filePath.toString().contains("_unstretched-hud.big")) {
            System.out.println("NEW " + gameId + ": Modified: " + filePath);
            HUD hud = game.getHud();
            hud.setOriginal(Files.notExists(filePath));
            game.setHud(hud);

            controller.updateHudBox(game.getId());
        }
    }

    public boolean areDifferent(Object current, Object previous) {
        return (current != null && previous != null && !current.equals(previous)) ||
                (current != null && previous == null) || (current == null && previous != null);
    }

    public void listenToRegistry() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (!isPaused()) // Don't poll while applying changes
                poll();
        }, 0, 1, TimeUnit.SECONDS);
    }
}
