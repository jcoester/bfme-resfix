package gui;

import controller.Controller;
import filemanager.BigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import filemanager.IniManager;
import model.*;
import model.updateCheck.Release;
import utility.*;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class MainFrame extends JFrame {
    // Logger
    public static final Logger logger = LoggerFactory.getLogger(MainFrame.class.getName());
    // Time
    private final long appStartTime = System.currentTimeMillis();
    // Controller
    private final Controller controller;
    // Resource bundles
    private ResourceBundle properties;
    private ResourceBundle labels;
    // UI Components
    private JPanel mainPanel;
    private JButton btnApply;
    private JProgressBar loadingBar;
    private JLabel loadingText;
    private GamePanel gamePanel1;
    private GamePanel gamePanel2;
    private GamePanel gamePanel3;

    public MainFrame() {
        GamePanel gamePanel1 = new GamePanel();
        GamePanel gamePanel2 = new GamePanel();
        GamePanel gamePanel3 = new GamePanel();

        // Init Resources
        loadResourceBundles();

        // Initialize MVC
        List<Game> games = GameInfo.initializeGames();
        Display display = new Display();
        View view = new View(gamePanel1, gamePanel2, gamePanel3, properties, labels, loadingBar, loadingText, btnApply);
        controller = new Controller(games, display, view);
        view.addController(controller);

        // Init UI
        applyUIFoundation();
        CompletableFuture<Void> appliedCustomFont = applyCustomFont();

        // Init ExceptionHandler
        CompletableFuture<Void> initializedExceptionHandler = initializeExceptionHandler();

        // Init ChangeListener listening for Display, Registry and File System changes
        ChangeListener listener = new ChangeListener(controller, mainPanel, labels);
        CompletableFuture<Void> initializedData = listener.poll();

        // Join CompletableFutures
        appliedCustomFont.join();
        initializedExceptionHandler.join();
        initializedData.join();

        // Once data is initialized
        CompletableFuture.allOf(appliedCustomFont, initializedExceptionHandler, initializedData).thenRun(() -> {
            SwingUtilities.invokeLater(() -> {
                // Show UI
                applySize();
                setLocationRelativeTo(null);
                setVisible(true);
                System.out.println(System.currentTimeMillis() - appStartTime + " : Show UI");

                // Add Button Listener
                applyButtonListener(listener);
                // Display Invalid Resolution Feedback
                for (Game game : controller.getGames())
                    listener.resolutionInvalidFeedback(game);
            });

            // Run Update Check
            CompletableFuture.runAsync(this::runUpdateCheck);

            // Start listening to System changes
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
                if (!listener.isPaused()) // Don't poll while applying changes
                    listener.poll();
            }, 0, 1, TimeUnit.SECONDS);
            listener.startListening();
        });
    }

    private void loadResourceBundles() {
        properties = ResourceBundle.getBundle("application");
        labels = ResourceBundle.getBundle("labels", Locale.getDefault()); // new Locale("es", "Es"));
    }

    private void applyUIFoundation() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/icon/icon-11.png"))).getImage());
        setTitle(labels.getString("app") + " ResFix v" + properties.getString("version"));
    }

    private CompletableFuture<Void> applyCustomFont() {
        return CompletableFuture.runAsync(() -> {
            // Set system properties for font antialiasing
            System.setProperty("awt.useSystemAAFontSettings", "lcd");
            System.setProperty("swing.aatext", "true");

            // Set and apply font
            Color gold = Color.decode("#6b520e");
            try (InputStream is = MainFrame.class.getClassLoader().getResourceAsStream("font/RingBearer.ttf")) {
                if (is != null) {
                    Font ringBearer = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 24);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ringBearer);
                    SwingUtilities.invokeLater(() -> {
                        gamePanel1.updateTitleFont(labels.getString("title.bfme1"), ringBearer, gold);
                        gamePanel2.updateTitleFont(labels.getString("title.bfme2"), ringBearer, gold);
                        gamePanel3.updateTitleFont(labels.getString("title.rotwk"), ringBearer, gold);
                    });
                }
            } catch (FontFormatException | IOException e) {
                logger.error("applyCustomFont", e);
            }
        });
    }

    private CompletableFuture<Void> initializeExceptionHandler() {
        return CompletableFuture.runAsync(() -> ExceptionHandler.setInstance(mainPanel, properties, labels));
    }

    public void applySize() {
        setMinimumSize(new Dimension(0,0));
        pack();
        setMinimumSize(getSize());
    }

    private void applyButtonListener(ChangeListener listener) {
        btnApply.addActionListener(e -> handleButtonPress(listener));
    }

    private void handleButtonPress(ChangeListener listener) {
        List<Game> games = controller.getGames();

        // Disable UI
        SwingUtilities.invokeLater(() -> {
            listener.pause();
            btnApply.setEnabled(false);
            loadingBar.setIndeterminate(true);
        });

        // Asynchronously Apply changes
        CompletableFuture
        .supplyAsync(() -> applyChanges(games))
        .thenAccept(changes ->
            SwingUtilities.invokeLater(() -> {
                // Enable UI
                listener.resume();
                btnApply.setEnabled(true);
                loadingBar.setIndeterminate(false);
                // Prompt changes
                if (changes)
                    JOptionPane.showMessageDialog(mainPanel, labels.getString("feedback.changesApplied"));
                else
                    JOptionPane.showMessageDialog(mainPanel, labels.getString("feedback.noChangesApplied"));
            }
        ));
    }

    private void runUpdateCheck() {
        System.out.println(System.currentTimeMillis() - appStartTime + " : Start Update Check");
        Release latestRelease = GitHub.getLatestRelease(properties);
        if (latestRelease != null) {
            String versionLocal = properties.getString("version");
            String versionReleased = latestRelease.tag_name;
            if (!versionLocal.equals(versionReleased)) {
                displayUpdatePrompt(latestRelease);
            }
        }
        System.out.println(System.currentTimeMillis() - appStartTime + " : Completed Update Check");
    }

    public void displayUpdatePrompt(Release latestRelease) {
        Object[] choices = {
                labels.getString("update.download"),
                labels.getString("update.later") };

        String title = String.format(labels.getString("update.title"), latestRelease.tag_name);
        String message = labels.getString("update.changesFrom") + " " + GitHub.getLocalDate(latestRelease.fileUpdatedAt) + ":\n" + latestRelease.body;

        int optionDialog = JOptionPane.showOptionDialog(
                mainPanel,
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
                Desktop.getDesktop().browse(new URI(latestRelease.fileDownloadURL));
                int confirmDialog = JOptionPane.showConfirmDialog(mainPanel,
                        String.format(labels.getString("update.newFile"), latestRelease.fileName),
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

    private boolean applyChanges(List<Game> games) {
        boolean changes = false;

        // Skip
        if (games.stream().noneMatch(Game::isInstalled))
            return false;

        // Save selection state

        Resolution res1 = gamePanel1.getResolutionSelectedItem();
        Resolution res2 = gamePanel2.getResolutionSelectedItem();
        Resolution res3 = gamePanel3.getResolutionSelectedItem();
        Maps maps1 = gamePanel1.getMapsSelectedItem();
        Maps maps2 = gamePanel2.getMapsSelectedItem();
        Maps maps3 = gamePanel3.getMapsSelectedItem();
        HUD hud1 = gamePanel1.getHUDSelectedItem();
        HUD hud2 = gamePanel2.getHUDSelectedItem();
        HUD hud3 = gamePanel3.getHUDSelectedItem();

        // Loop games
        for (Game game : games) {

            // Skip
            if (!game.isInstalled() || game.isRunning())
                continue;

            // Resolution
            Resolution selectedResolution = null;
            switch (game.getId()) {
                case BFME1: selectedResolution = res1; break;
                case BFME2: selectedResolution = res2; break;
                case ROTWK: selectedResolution = res3; break;
            }
            if (selectedResolution != null && !selectedResolution.equals(game.getInGameResolution()) && selectedResolution.getWidth() != -1) {
                handleResolutionSelection(game, selectedResolution);
                changes = true;
            }

            // Maps
            Maps selectedMaps = null;
            switch (game.getId()) {
                case BFME1: selectedMaps = maps1; break;
                case BFME2: selectedMaps = maps2; break;
                case ROTWK: selectedMaps = maps3; break;
            }
            // Handle selection
            if (selectedMaps != null && !selectedMaps.equals(game.getMaps()) && game.isPatched()) {
                handleMapsSelection(game, selectedMaps);
                changes = true;
            }

            // HUD
            HUD selectedHUD = null;
            switch (game.getId()) {
                case BFME1: selectedHUD = hud1; break;
                case BFME2: selectedHUD = hud2; break;
                case ROTWK: selectedHUD = hud3; break;
            }
            // Handle selection
            if (selectedHUD != null && !selectedHUD.equals(game.getHud())) {
                handleHudSelection(game, selectedHUD);
                changes = true;
            }
        }
        return changes;
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
            BigManager.downloadBigFile(mapsFile, maps.getFilePath(), loadingBar, loadingText, labels);
        }

        // C: Create Backup, then override
        if (selectedMap.getAspectRatio() != null && maps.getAspectRatio() == null) {
            logger.info("handleMapsSelection(): Create Backup, then override");
            boolean result = BigManager.copyBigFileFromTo(maps.getFilePath(), maps.getBackupPath(), labels);
            if (result) BigManager.downloadBigFile(mapsFile, maps.getFilePath(), loadingBar, loadingText, labels);
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
            BigManager.downloadBigFile(GitHub.getHudURL(game.getId(), properties), hud.getFilePath(), loadingBar, loadingText, labels);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Iterate over the components in the content pane
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;

                // Apply rendering hints for text antialiasing
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw the label's text with antialiasing
                label.paint(g2d);

                // Dispose the graphics context to release system resources
                g2d.dispose();
            }
        }
    }
}
