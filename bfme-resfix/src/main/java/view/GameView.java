package view;

import controller.Controller;
import filemanager.PatchManager;
import helper.SoundPlayer;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameView extends JPanel {
    private final List<Boolean> patchLabels = new ArrayList<>(Collections.nCopies(3, false));
    private final boolean[] isProgrammaticUpdate = {false}; // Flag to indicate programmatic updates
    private static final String HOVER_WAV = "Hover.wav";
    private static final String PRESS_WAV = "Press.wav";
    private ResourceBundle labels;
    private ResourceBundle properties;
    private MainView mainView;
    private Controller controller;
    private JComboBox<Resolution> comboBoxRes;
    private JComboBox<Maps> comboBoxMaps;
    private JComboBox<HUD> comboBoxHud;
    private JLabel labelRunning;
    private JLabel labelInstall;
    private JLabel labelSpacer;
    private JLabel labelPatch;
    private JLabel titleBFME;
    private JLabel titleRes;
    private JLabel titleMaps;
    private JLabel titleHud;
    private JPanel root;

    public GameView() {
        setupComboBoxSounds(comboBoxRes);
        setupComboBoxSounds(comboBoxMaps);
        setupComboBoxSounds(comboBoxHud);
    }

    public Resolution getResolutionSelectedItem() {
        return (Resolution) comboBoxRes.getSelectedItem();
    }

    public Maps getMapsSelectedItem() {
        return (Maps) comboBoxMaps.getSelectedItem();
    }

    public HUD getHUDSelectedItem() {
        return (HUD) comboBoxHud.getSelectedItem();
    }

    public void updateTitleFont(String text, Font font, Color color) {
        titleBFME.setText(text);
        titleBFME.setFont(font);
        titleBFME.setForeground(color);
        titleBFME.repaint();
    }

    public void render(Game game, Display display) {
        enableLabel(game);
        setInstallationLabel(game);
        setRunningLabel(game);
        setComboBoxResolution(game, display);
        setComboBoxMaps(game);
        setComboBoxHud(game);
    }

    private void enableLabel(Game game) {
        titleBFME.setEnabled(game.isInstalled());
        titleRes.setEnabled(game.isInstalled());
        titleMaps.setEnabled(game.isInstalled());
        titleHud.setEnabled(game.isInstalled());
    }

    private void setInstallationLabel(Game game) {
        labelInstall.setEnabled(game.isInstalled());
        labelInstall.setText(labels.getString("game.notInstalled"));
        labelSpacer.setVisible(game.isInstalled() && !game.isPatched());
        labelPatch.setVisible(game.isInstalled() && !game.isPatched());
        labelPatch.setEnabled(game.isInstalled() && !game.isPatched() && !game.isRunning());
        labelPatch.setCursor(Cursor.getDefaultCursor());

        // Reset mouse listener
        int index = game.getId().ordinal();
        MouseListener[] listeners = labelPatch.getMouseListeners();
        for (MouseListener l : listeners)
            labelPatch.removeMouseListener(l);
        patchLabels.set(index, false);

        if (game.isInstalled()) {
            labelInstall.setText(getVersionLabel(game));

            if (!game.isPatched()) {
                labelPatch.setText(getPatchHereLabel(game));

                if (!game.isRunning()) {
                    labelPatch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                    // Allow only one instance per label
                    if (!patchLabels.get(index)) {
                        patchLabels.set(index, true); // Mark as added

                        labelPatch.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                SoundPlayer.playSound("labelPatch", HOVER_WAV, "mouseEntered");
                            }

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                SoundPlayer.playSound("labelPatch", PRESS_WAV, "mouseClicked");

                                SwingUtilities.invokeLater(() -> {
                                    mainView.disableUI();
                                    // Reset label
                                    labelPatch.removeMouseListener(this);
                                    labelPatch.setCursor(Cursor.getDefaultCursor());
                                    labelPatch.setText(getPatchLoadingLabel(game));
                                });

                                CompletableFuture
                                .runAsync(() ->
                                    PatchManager.patchGame(game, properties, labels, mainView.getProgressBar(), mainView.getProgressText()))
                                .thenRun(() ->
                                    SwingUtilities.invokeLater(() -> {
                                        controller.updateGameView(game.getId());
                                        mainView.resetProgress();
                                        mainView.enableUI();
                                    })
                                );

                                patchLabels.set(index, false); // Mark as removed
                            }
                        });
                    }
                }
            }
        }
    }

    private void setRunningLabel(Game game) {
        labelRunning.setText(null);
        labelRunning.setEnabled(game.isRunning());

        if (game.isRunning())
            labelRunning.setText(getRunningDisclaimer());
    }

    private void setComboBoxResolution(Game game, Display display) {
        isProgrammaticUpdate[0] = true;

        // Default
        comboBoxRes.removeAllItems();
        comboBoxRes.setEnabled(false);
        comboBoxRes.setRenderer(new CustomComboBoxRenderer());

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        comboBoxRes.setEnabled(!game.isRunning());

        // Add items
        List<Resolution> allResolutions = display.getAllResolutions();
        for (Resolution res : allResolutions) {
            boolean isRecommended = res.equals(display.getRecommendedRes());
            boolean isInGame = res.equals(game.getInGameResolution());

            // Recommended
            if (isRecommended && !isInGame)
                if (res.getAspectRatio().equals("32:9"))  // Do not recommend 32:9
                    comboBoxRes.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio()));
                else
                    comboBoxRes.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio(), labels.getString("cb.recommended")));

            // In-Game
            else if (isInGame)
                comboBoxRes.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio(), labels.getString("cb.inGame")));
            // Default
            else
                comboBoxRes.addItem(res);
        }

        // Empty Placeholder
        if (game.getInGameResolution() == null) {
            addAndSelectPlaceholder(comboBoxRes, labels);
        }

        // Selection
        if (game.getInGameResolution() != null) {
            if (allResolutions.contains(game.getInGameResolution()))
                comboBoxRes.setSelectedItem(game.getInGameResolution()); // Set in-game resolution to ComboBox
            else
                addAndSelectPlaceholder(comboBoxRes, labels); // If not available, set placeholder
        }

        isProgrammaticUpdate[0] = false;
    }

    private void addAndSelectPlaceholder(JComboBox<Resolution> comboBox, ResourceBundle labels) {
        Resolution placeholderNoRes = new Resolution(-1, -1, "", labels.getString("cb.pleaseChoose"));
        comboBox.addItem(placeholderNoRes);
        comboBox.setSelectedItem(placeholderNoRes);
    }

    private void setComboBoxMaps(Game game) {
        isProgrammaticUpdate[0] = true;

        // Default
        comboBoxMaps.removeAllItems();
        comboBoxMaps.setEnabled(false);
        comboBoxMaps.setRenderer(new CustomComboBoxRenderer());

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        if (game.isPatched())
            comboBoxMaps.setEnabled(true);

        if (game.isRunning())
            comboBoxMaps.setEnabled(false);

        // Add items
        Maps m21_9 = new Maps(game.getInstallationPath(), "21:9", labels.getString("cb.maps.default"));
        Maps m16_9 = new Maps(game.getInstallationPath(), "16:9", labels.getString("cb.maps.default"));
        Maps m4_3 = new Maps(game.getInstallationPath(), "4:3", labels.getString("cb.maps.default"));
        comboBoxMaps.addItem(m21_9);
        comboBoxMaps.addItem(m16_9);
        comboBoxMaps.addItem(m4_3);

        // 4:3 In-Game
        if ("4:3".equals(game.getMaps().getAspectRatio())) {
            m4_3.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBoxMaps.setSelectedItem(m4_3);
        }
        // 16:9 In-Game
        if ("16:9".equals(game.getMaps().getAspectRatio())) {
            m16_9.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBoxMaps.setSelectedItem(m16_9);
        }
        // 21:9 In-Game
        if ("21:9".equals(game.getMaps().getAspectRatio())) {
            m21_9.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBoxMaps.setSelectedItem(m21_9);
        }
        // User-modified In-Game
        if (game.getMaps().getAspectRatio() == null) {
            Maps modified = new Maps(game.getInstallationPath(), null, labels.getString("cb.maps.modified") + " -- " + labels.getString("cb.inGame"));
            comboBoxMaps.addItem(modified);
            comboBoxMaps.setSelectedItem(modified);
        }
        // Option Modified Maps through Backup
        if (game.getMaps().getAspectRatio() != null && game.getMaps().isBackup()) {
            Maps custom = new Maps(game.getInstallationPath(), null, labels.getString("cb.maps.modified"));
            comboBoxMaps.addItem(custom);
        }
        // Placeholder
        if (!game.isPatched()) {
            Maps placeholder = new Maps(game.getMaps().getFilePath(), null, String.format(labels.getString("cb.patchRequired"), game.getVersionAvailablePatch()));
            comboBoxMaps.addItem(placeholder);
            comboBoxMaps.setSelectedItem(placeholder);
        }

        isProgrammaticUpdate[0] = false;
    }

    private void setComboBoxHud(Game game) {
        isProgrammaticUpdate[0] = true;

        // Default
        comboBoxHud.removeAllItems();
        comboBoxHud.setEnabled(false);
        comboBoxHud.setRenderer(new CustomComboBoxRenderer());

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        comboBoxHud.setEnabled(!game.isRunning());

        // Add items
        HUD hud16_9 = new HUD(game.getHud().getFilePath(), false, "16:9 / 21:9");
        HUD hud4_3 = new HUD(game.getHud().getFilePath(), true, "4:3");
        comboBoxHud.addItem(hud16_9);
        comboBoxHud.addItem(hud4_3);

        // Default HUD
        if (game.getHud().isOriginal()) {
            hud4_3.setLabel(hud4_3.getLabel() + " -- " + labels.getString("cb.inGame"));
            comboBoxHud.setSelectedItem(hud4_3);
            // Modded HUD
        } else {
            hud16_9.setLabel(hud16_9.getLabel() + " -- " + labels.getString("cb.inGame"));
            comboBoxHud.setSelectedItem(hud16_9);
        }

        isProgrammaticUpdate[0] = false;
    }

    private String getVersionLabel(Game game) {
        return "<html><font color='green'>" + labels.getString("game.installed") + game.getVersionInstalled() + "</font></html>";
    }

    private String getPatchHereLabel(Game game) {
        return "<html><font color='blue'><u>" + String.format(labels.getString("game.patchHere"), game.getVersionAvailablePatch() + "</u></font></html>");
    }

    private String getRunningDisclaimer() {
        return "<html><font color='red'>" + String.format(labels.getString("game.running") + "</font></html>");
    }

    private String getPatchLoadingLabel(Game game) {
        return String.format(labels.getString("game.patchLoading"), game.getVersionAvailablePatch());
    }

    public void passRefs(ResourceBundle labels, ResourceBundle properties, MainView mainView, Controller controller) {
        this.labels = labels;
        this.properties = properties;
        this.mainView = mainView;
        this.controller = controller;
    }

    public void renderResBox(Game game, Display display) {
        setComboBoxResolution(game, display);
    }

    public void renderMapsBox(Game game) {
        setComboBoxMaps(game);
    }

    public void renderHudBox(Game game) {
        setComboBoxHud(game);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public <T> void setupComboBoxSounds(JComboBox<T> comboBox) {
        comboBox.addActionListener(e -> {
            // Check if the update was programmatic
            if (isProgrammaticUpdate[0]) {
                return; // Do nothing if the update was programmatic
            }
            SoundPlayer.playSound(comboBox.getUIClassID(), "Press.wav", "actionPerformed");
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Iterate over the components in the content pane
        for (Component component : root.getComponents()) {
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
