package controller;

import filemanager.BigManager;
import filemanager.IniManager;
import listener.ChangeListener;
import helper.ExceptionHandler;
import helper.GitHub;
import model.*;
import model.updateCheck.Release;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static main.Main.logger;
import static helper.GameInfo.isInvalidResolution;
import static model.GameID.*;

public class Controller {
    private final ResourceBundle properties;
    private final ResourceBundle labels;
    private final List<Game> games;
    private Display display;
    private final MainView mainView;
    private ChangeListener listener;

    public Controller(ResourceBundle properties, ResourceBundle labels, List<Game> games, Display display, MainView mainView) {
        this.properties = properties;
        this.labels = labels;
        this.games = games;
        this.display = display;
        this.mainView = mainView;
    }

    public void updateDisplay(Display display) {
        this.display = display;
    }

    public void updateGameView(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderGame(getGame(gameID), display));
    }

    public Game getGame(GameID gameId) {
        for (Game game : games) {
            if (game.getId() == gameId)
                return game;
        }
        return null;
    }

    public List<Game> getGames() {
        return games;
    }

    public Display getDisplay() {
        return display;
    }

    public void showUI() {
        SwingUtilities.invokeLater(() -> {
            // Resize
            mainView.applyResize();

            // Show
            mainView.setLocationRelativeTo(null);
            mainView.setVisible(true);

            // Add Button Listener
            mainView.addButtonListener(e -> handleButtonPress());

            // Display Invalid Resolution Feedback
            for (Game game : games)
                resolutionInvalidFeedback(game);
        });
    }

    public CompletableFuture<Void> initCustomFont() {
        return CompletableFuture.runAsync(this::applyCustomFont);
    }

    private void applyCustomFont() {
        // Set system properties for font antialiasing
        System.setProperty("awt.useSystemAAFontSettings", "lcd");
        System.setProperty("swing.aatext", "true");

        // Set and apply font
        try (InputStream is = MainView.class.getClassLoader().getResourceAsStream("font/RingBearer.ttf")) {
            if (is != null) {
                Font ringBearer = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 24);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ringBearer);
                SwingUtilities.invokeLater(() -> mainView.updateGameTitlesFont(ringBearer, Color.decode("#6b520e")));
            }
        } catch (FontFormatException | IOException e) {
            logger.error("applyCustomFont", e);
        }
    }

    private void handleButtonPress() {
        List<Game> games = getGames();

        // Disable UI
        SwingUtilities.invokeLater(mainView::disableUI);

        // Asynchronously Apply changes
        CompletableFuture
        .supplyAsync(() -> applyChanges(games))
        .thenAccept(changes ->
            SwingUtilities.invokeLater(() -> {
                // Enable UI
                mainView.enableUI();

                // Prompt changes
                if (changes) {
                    JOptionPane.showMessageDialog(mainView, labels.getString("feedback.changesApplied"));
                } else {
                    JOptionPane.showMessageDialog(mainView, labels.getString("feedback.noChangesApplied"));
                }
            }
        ));
    }

    private boolean applyChanges(List<Game> games) {
        boolean changes = false;

        // Skip
        if (games.stream().noneMatch(Game::isInstalled))
            return false;

        // Loop games
        for (Game game : games) {
            // Skip
            if (!game.isInstalled() || game.isRunning())
                continue;

            // Resolution
            Resolution selectedRes = mainView.getGameView(game.getId()).getResolutionSelectedItem();
            if (selectedRes != null && !selectedRes.equals(game.getInGameResolution()) && selectedRes.getWidth() != -1) {
                listener.pause();
                handleResolutionSelection(game, selectedRes);
                listener.resume();
                changes = true;
            }

            // Maps
            Maps selectedMaps = mainView.getGameView(game.getId()).getMapsSelectedItem();
            if (selectedMaps != null && !selectedMaps.equals(game.getMaps()) && game.isPatched()) {
                listener.pause();
                handleMapsSelection(game, selectedMaps);
                listener.resume();
                changes = true;
            }

            // HUD
            HUD selectedHUD = mainView.getGameView(game.getId()).getHUDSelectedItem();
            if (selectedHUD != null && !selectedHUD.equals(game.getHud())) {
                listener.pause();
                handleHudSelection(game, selectedHUD);
                listener.resume();
                changes = true;
            }

            // DVD
            DVD selectedDVD = mainView.getGameView(game.getId()).getDVDSelectedItem();
            if (selectedDVD != null && !selectedDVD.equals(game.getDvd())) {
                listener.pause();
                handleDVDSelection(game, selectedDVD);
                listener.resume();
                changes = true;
            }

            // DVD
            Intro selectedIntro = mainView.getGameView(game.getId()).getIntroSelectedItem();
            if (selectedIntro != null && !selectedIntro.equals(game.getIntro())) {
                listener.pause();
                handleIntroSelection(game, selectedIntro);
                listener.resume();
                changes = true;
            }
        }
        return changes;
    }

    private void handleIntroSelection(Game game, Intro selectedIntro) {
        logger.info("handleIntroSelection(): {}: {} > {}", game.getId(), game.getIntro().isOriginal(), selectedIntro.isOriginal());

        Path root = Paths.get(game.getInstallationPath() + "/data/movies/");
        List<Path> fileList = new ArrayList<>();
        fileList.add(Paths.get(root + "/NewLineLogo.vp6"));
        fileList.add(Paths.get(root + "/EALogo.vp6")); // ALL
        if (game.getId().equals(BFME1)) {
            fileList.add(Paths.get(root + "/intel.vp6"));
            fileList.add(Paths.get(root + "/THX.vp6"));
        } else if (game.getId().equals(BFME2) || game.getId().equals(ROTWK))  {
            fileList.add(Paths.get(root + "/NLC_LOGO.vp6"));
            fileList.add(Paths.get(root + "/TE_LOGO.vp6"));
        }

        // A: Restore Original from Backup
        if (selectedIntro.isOriginal()) {
            logger.info("handleIntroSelection(): Restore Original from Backup");
            for (Path file : fileList) {
                Path backupFile = Paths.get(file + ".bak");
                boolean result = BigManager.copyBigFileFromTo(backupFile, file, labels); // Restore from Backup
                if (result) BigManager.removeBigFile(backupFile, labels); // Delete previous Backup
            }

        // B: Create Backup, then remove
        } else {
            for (Path file : fileList) {
                Path backupFile = Paths.get(file + ".bak");
                boolean result = BigManager.copyBigFileFromTo(file, backupFile, labels); // Restore from Backup
                if (result) BigManager.removeBigFile(file, labels); // Delete previous Backup
            }
        }
    }

    private void handleDVDSelection(Game game, DVD selectedDVD) {
        logger.info("handleDVDSelection(): {}: {} > {}", game.getId(), game.getDvd().isOriginal(), selectedDVD.isOriginal());
        DVD dvd = game.getDvd();

        // A: Restore Original from Backup
        if (selectedDVD.isOriginal()) {
            logger.info("handleDVDSelection(): Restore Original from Backup");
            boolean result = BigManager.copyBigFileFromTo(dvd.getBackupPath(), dvd.getFilePath(), labels); // Restore from Backup
            if (result) BigManager.removeBigFile(dvd.getBackupPath(), labels); // Delete previous Backup

        // B: Create Backup, then override
        } else {
            URL dvdFile = GitHub.getDvdURL(game.getId(), properties);
            logger.info("handleDVDSelection(): Create Backup, then override");
            boolean result = BigManager.copyBigFileFromTo(dvd.getFilePath(), dvd.getBackupPath(), labels);
            if (result) BigManager.downloadBigFile(dvdFile, dvd.getFilePath(), mainView.getProgressBar(), mainView.getProgressText(), labels);
        }
    }

    private void handleResolutionSelection(Game game, Resolution selectedResolution) {
        logger.info("handleResolutionSelection(): {}: {} > {}", game.getId(), game.getInGameResolution(), selectedResolution);

        // A: No in-game resolution yet
        if (game.getInGameResolution() == null)
            IniManager.writeNewFile(game, selectedResolution, labels); // Write new file

        // B: Existing in-game resolution
        if (game.getInGameResolution() != null)
            IniManager.writeNewResolutionToFile(game.getUserDataPath(), selectedResolution, labels); // Update file
    }

    public void handleMapsSelection(Game game, Maps selectedMap) {
        logger.info("handleMapsSelection(): {}: {} > {}", game.getId(), game.getMaps().getAspectRatio(), selectedMap.getAspectRatio());
        Maps maps = game.getMaps();

        // A: Restore Modified Maps from Backup
        if (selectedMap.getAspectRatio() == null) {
            logger.info("handleMapsSelection(): Restore Modified Maps from Backup");
            boolean result = BigManager.copyBigFileFromTo(maps.getBackupPath(), maps.getFilePath(), labels); // Restore from Backup
            if (result) BigManager.removeBigFile(maps.getBackupPath(), labels); // Delete previous Backup
        }

        // B & C: Prepare download URL
        URL mapsFile = GitHub.getMapsURL(game.getId(), selectedMap.getAspectRatio(), properties);

        // B: Override Standard Map
        if (selectedMap.getAspectRatio() != null && maps.getAspectRatio() != null) {
            logger.info("handleMapsSelection(): Override Standard Map");
            BigManager.downloadBigFile(mapsFile, maps.getFilePath(), mainView.getProgressBar(), mainView.getProgressText(), labels);
        }

        // C: Create Backup, then override
        if (selectedMap.getAspectRatio() != null && maps.getAspectRatio() == null) {
            logger.info("handleMapsSelection(): Create Backup, then override");
            boolean result = BigManager.copyBigFileFromTo(maps.getFilePath(), maps.getBackupPath(), labels);
            if (result) BigManager.downloadBigFile(mapsFile, maps.getFilePath(), mainView.getProgressBar(), mainView.getProgressText(), labels);
        }
    }

    private void handleHudSelection(Game game, HUD selectedHUD) {
        logger.info("handleHudSelection(): {}: {} > {}", game.getId(), game.getHud(), selectedHUD);
        HUD hud = game.getHud();

        // Switch to 4:3 Original HUD
        if (selectedHUD.isOriginal())
            BigManager.removeBigFile(hud.getFilePath(), labels);

        // Switch to 16:9 / 21:9 HUD
        if (!selectedHUD.isOriginal())
            BigManager.downloadBigFile(GitHub.getHudURL(game.getId(), properties), hud.getFilePath(), mainView.getProgressBar(), mainView.getProgressText(), labels);
    }

    public void runUpdateCheck() {
        CompletableFuture.runAsync(() -> {
            Release latestRelease = GitHub.getLatestRelease(properties);
            if (latestRelease != null) {
                String versionLocal = properties.getString("version");
                String versionReleased = latestRelease.getTag_name();
                if (!versionLocal.equals(versionReleased)) {
                    displayUpdatePrompt(latestRelease);
                }
            }
        });
    }

    public void displayUpdatePrompt(Release latestRelease) {
        Object[] choices = {
                labels.getString("update.download"),
                labels.getString("update.later") };

        String title = String.format(labels.getString("update.title"), latestRelease.getTag_name());
        String message = labels.getString("update.changesFrom") + " " + GitHub.getLocalDate(latestRelease.getFileUpdatedAt()) + ":\n" + latestRelease.getBody();

        int optionDialog = JOptionPane.showOptionDialog(
                mainView,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                choices,
                choices[0]);

        if (optionDialog == JOptionPane.YES_OPTION)
            displayFileDownloadedPrompt(latestRelease);
    }

    private void displayFileDownloadedPrompt(Release latestRelease) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(latestRelease.getFileDownloadURL()));
                int confirmDialog = JOptionPane.showConfirmDialog(mainView,
                        String.format(labels.getString("update.newFile"), latestRelease.getFileName()),
                        labels.getString("update.successful"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (confirmDialog == JOptionPane.OK_OPTION)
                    System.exit(0);
            } catch (IOException | URISyntaxException e) {
                logger.error("Error occurred in displayFileDownloadedPrompt()", e);
            }
        }
    }

    public void resolutionInvalidFeedback(Game game) {
        if (isInvalidResolution(game, display))
            JOptionPane.showMessageDialog(mainView, labels.getString("app") + " " + (game.getId().ordinal()+1) + ": " +
                    String.format(labels.getString("feedback.resolution.notSupported"), game.getInGameResolution()));
    }

    public void resolutionTooHighFeedback(Game game) {
        if (game.isRunning() || !game.isInstalled() || game.getInGameResolution() == null || isInvalidResolution(game, getDisplay()))
            return;

        // Resolution Too High
        if (game.getInGameResolution().getWidth() > getDisplay().getRecommendedRes().getWidth() ||
                game.getInGameResolution().getHeight() > getDisplay().getRecommendedRes().getHeight())
            JOptionPane.showMessageDialog(mainView, labels.getString("app") + " " + (game.getId().ordinal()+1) + ": " +
                    labels.getString("feedback.resolution.tooHigh"));
    }

    public void resolution32_9Feedback(Game game) {
        if (game.isRunning() || !game.isInstalled() || game.getInGameResolution() == null || isInvalidResolution(game, getDisplay()))
            return;

        // Resolution 32:9
        if (game.getInGameResolution().getAspectRatio().equals("32:9"))
            JOptionPane.showMessageDialog(mainView, labels.getString("app") + " " + (game.getId().ordinal()+1) + ": " +
                    labels.getString("feedback.resolution.32_9"));
    }

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    public CompletableFuture<Void> initExHandler() {
        return CompletableFuture.runAsync(() -> ExceptionHandler.setInstance(mainView, properties, labels));
    }

    public void updateResBox(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderResBox(getGame(gameID), display));
    }

    public void updateMapsBox(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderMapsBox(getGame(gameID)));
    }

    public void updateHudBox(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderHudBox(getGame(gameID)));
    }

    public void updateDVDBox(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderDVDBox(getGame(gameID)));
    }

    public void updateIntroBox(GameID gameID) {
        SwingUtilities.invokeLater(() -> mainView.renderIntroBox(getGame(gameID)));
    }

    public void updateAllResBoxes() {
        SwingUtilities.invokeLater(() -> {
            mainView.renderResBox(getGame(BFME1), display);
            mainView.renderResBox(getGame(BFME2), display);
            mainView.renderResBox(getGame(ROTWK), display);
        });
    }


}
