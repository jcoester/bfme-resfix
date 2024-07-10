package view;

import controller.Controller;
import filemanager.PatchManager;
import gui.GamePanel;
import model.*;
import utility.CustomComboBoxRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class View {

    public final List<Boolean> patchLabels = new ArrayList<>(Collections.nCopies(3, false));
    private Controller controller;
    private final List<GamePanel> gamePanels;
    private final ResourceBundle labels;
    private final ResourceBundle properties;
    private final JProgressBar loadingBar;
    private final JLabel loadingText;
    private final JButton btnApply;

    public View(List<GamePanel> gamePanels, ResourceBundle properties, ResourceBundle labels,
                JProgressBar loadingBar, JLabel loadingText, JButton btnApply) {

        this.gamePanels = gamePanels;
        this.properties = properties;
        this.labels = labels;
        this.loadingBar = loadingBar;
        this.loadingText = loadingText;
        this.btnApply = btnApply;
    }

    public void render(Display display) {
        for (GameID gameID : GameID.values())
            renderGame(gameID, display);
    }

    public void renderGame(GameID gameID, Display display) {
        renderGame(gamePanels.get(gameID.ordinal()), controller.getGame(gameID), display);
    }

    private void renderGame(GamePanel gamePanel, Game game, Display display) {
        if (game == null)
            return;


        gamePanel.getTitleBFME().setEnabled(game.isInstalled());
        gamePanel.getTitleRes().setEnabled(game.isInstalled());
        gamePanel.getTitleMaps().setEnabled(game.isInstalled());
        gamePanel.getTitleHud().setEnabled(game.isInstalled());

        setInstallationLabel(gamePanel, game);
        setRunningLabel(gamePanel, game);
        setComboBoxResolution(gamePanel, game, labels, display);
        setComboBoxMaps(gamePanel, game, labels);
        setComboBoxHud(gamePanel, game, labels);
    }

    private void setRunningLabel(GamePanel gamePanel, Game game) {
        JLabel labelRunning = gamePanel.getLabelRunning();

        labelRunning.setText(null);
        labelRunning.setEnabled(game.isRunning());

        if (game.isRunning())
            labelRunning.setText(getRunningDisclaimer());
    }

    private void setComboBoxHud(GamePanel gamePanel, Game game, ResourceBundle labels) {
        JComboBox<HUD> comboBox = gamePanel.getJComboBoxHud();

        // Default
        comboBox.removeAllItems();
        comboBox.setEnabled(false);
        comboBox.setRenderer(new CustomComboBoxRenderer<>(comboBox.getRenderer()));

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        comboBox.setEnabled(!game.isRunning());

        // Add items
        HUD hud16_9 = new HUD(game.getHud().getFilePath(), false, "16:9 / 21:9");
        HUD hud4_3 = new HUD(game.getHud().getFilePath(), true, "4:3");
        comboBox.addItem(hud16_9);
        comboBox.addItem(hud4_3);

        // Default HUD
        if (game.getHud().isOriginal()) {
            hud4_3.setLabel(hud4_3.getLabel() + " -- " + labels.getString("cb.inGame"));
            comboBox.setSelectedItem(hud4_3);
        // Modded HUD
        } else {
            hud16_9.setLabel(hud16_9.getLabel() + " -- " + labels.getString("cb.inGame"));
            comboBox.setSelectedItem(hud16_9);
        }
    }

    private void setComboBoxMaps(GamePanel gamePanel, Game game, ResourceBundle labels) {
        JComboBox<Maps> comboBox = gamePanel.getJComboBoxMaps();

        // Default
        comboBox.removeAllItems();
        comboBox.setEnabled(false);
        comboBox.setRenderer(new CustomComboBoxRenderer<>(comboBox.getRenderer()));

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        if (game.isPatched())
            comboBox.setEnabled(true);

        if (game.isRunning())
            comboBox.setEnabled(false);

        // Add items
        Maps m21_9 = new Maps(game.getInstallationPath(), "21:9", labels.getString("cb.maps.default"));
        Maps m16_9 = new Maps(game.getInstallationPath(), "16:9", labels.getString("cb.maps.default"));
        Maps m4_3 = new Maps(game.getInstallationPath(), "4:3", labels.getString("cb.maps.default"));
        comboBox.addItem(m21_9);
        comboBox.addItem(m16_9);
        comboBox.addItem(m4_3);

        // 4:3 In-Game
        if ("4:3".equals(game.getMaps().getAspectRatio())) {
            m4_3.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBox.setSelectedItem(m4_3);
        }
        // 16:9 In-Game
        if ("16:9".equals(game.getMaps().getAspectRatio())) {
            m16_9.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBox.setSelectedItem(m16_9);
        }
        // 21:9 In-Game
        if ("21:9".equals(game.getMaps().getAspectRatio())) {
            m21_9.setLabel(labels.getString("cb.maps.default") + " -- " + labels.getString("cb.inGame"));
            comboBox.setSelectedItem(m21_9);
        }
        // User-modified In-Game
        if (game.getMaps().getAspectRatio() == null) {
            Maps modified = new Maps(game.getInstallationPath(), null, labels.getString("cb.maps.modified") + " -- " + labels.getString("cb.inGame"));
            comboBox.addItem(modified);
            comboBox.setSelectedItem(modified);
        }
        // Option Modified Maps through Backup
        if (game.getMaps().getAspectRatio() != null && game.getMaps().isBackup()) {
            Maps custom = new Maps(game.getInstallationPath(), null, labels.getString("cb.maps.modified"));
            comboBox.addItem(custom);
        }
        // Placeholder
        if (!game.isPatched()) {
            Maps placeholder = new Maps(game.getMaps().getFilePath(), null, String.format(labels.getString("cb.patchRequired"), game.getVersionAvailablePatch()));
            comboBox.addItem(placeholder);
            comboBox.setSelectedItem(placeholder);
        }
    }

    private void setComboBoxResolution(GamePanel gamePanel, Game game, ResourceBundle labels, Display display) {
        JComboBox<Resolution> comboBox = gamePanel.getJComboBoxRes();

        // Default
        comboBox.removeAllItems();
        comboBox.setEnabled(false);
        comboBox.setRenderer(new CustomComboBoxRenderer<>(comboBox.getRenderer()));

        // Skip
        if (!game.isInstalled())
            return;

        // Enable
        comboBox.setEnabled(!game.isRunning());

        // Add items
        List<Resolution> allResolutions = display.getAllResolutions();
        for (Resolution res : allResolutions) {
            boolean isRecommended = res.equals(display.getRecommendedRes());
            boolean isInGame = res.equals(game.getInGameResolution());

            // Recommended
            if (isRecommended && !isInGame)
                if (res.getAspectRatio().equals("32:9"))  // Do not recommend 32:9
                    comboBox.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio()));
                else
                    comboBox.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio(), labels.getString("cb.recommended")));

            // In-Game
            else if (isInGame)
                comboBox.addItem(new Resolution(res.getWidth(), res.getHeight(), res.getAspectRatio(), labels.getString("cb.inGame")));

            // Default
            else
                comboBox.addItem(res);
        }

        // Empty Placeholder
        if (game.getInGameResolution() == null) {
            addAndSelectPlaceholder(comboBox, labels);
        }

        // Selection
        if (game.getInGameResolution() != null) {
            if (allResolutions.contains(game.getInGameResolution()))
                comboBox.setSelectedItem(game.getInGameResolution()); // Set in-game resolution to ComboBox
            else
                addAndSelectPlaceholder(comboBox, labels); // If not available, set placeholder
        }
    }

    private void addAndSelectPlaceholder(JComboBox<Resolution> comboBox, ResourceBundle labels) {
        Resolution placeholderNoRes = new Resolution(-1, -1, "", labels.getString("cb.pleaseChoose"));
        comboBox.addItem(placeholderNoRes);
        comboBox.setSelectedItem(placeholderNoRes);
    }

    private void setInstallationLabel(GamePanel gamePanel, Game game) {
        JLabel labelInstall = gamePanel.getLabelInstall();
        JLabel labelSpacer = gamePanel.getLabelSpacer();
        JLabel labelPatch = gamePanel.getLabelPatch();

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

        if (game.isInstalled())
            labelInstall.setText(getVersionLabel(game));

        if (game.isInstalled() && !game.isPatched()) {
            labelPatch.setText(getPatchHereLabel(game));

            if (!game.isRunning()) {
                labelPatch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // Allow only one instance per label
                if (!patchLabels.get(index)) {
                    patchLabels.set(index, true); // Mark as added

                    labelPatch.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(() -> {
                                // Reset label
                                labelPatch.removeMouseListener(this);
                                labelPatch.setCursor(Cursor.getDefaultCursor());
                                labelPatch.setText(getPatchLoadingLabel(game));
                                // Set Loading UI
                                btnApply.setEnabled(false);
                                loadingBar.setIndeterminate(true);
                            });

                            CompletableFuture
                            .runAsync(() -> PatchManager.patchGame(game, properties, labels, loadingBar, loadingText))
                            .thenRun(() ->
                                // Reset Loading UI on completion
                                SwingUtilities.invokeLater(() -> {
                                    controller.updateView();
                                    loadingBar.setValue(0);
                                    loadingText.setText(".");
                                    btnApply.setEnabled(true);
                                    loadingBar.setIndeterminate(false);
                                })
                            );

                            patchLabels.set(index, false); // Mark as removed
                        }
                    });
                }
            }
        }
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

    public void addController(Controller controller) {
        this.controller = controller;
    }
}