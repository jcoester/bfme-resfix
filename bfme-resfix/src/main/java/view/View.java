package view;

import controller.Controller;
import filemanager.PatchManager;
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

    private final JLabel titleBFME1;
    private final JLabel titleBFME2;
    private final JLabel titleBFME3;
    private final JLabel title1Res, title2Res, title3Res;
    private final JLabel title1Maps, title2Maps, title3Maps;
    private final JLabel title1Hud, title2Hud, title3Hud;
    private final JComboBox<Resolution> comboBox1Res;
    private final JComboBox<Resolution> comboBox2Res;
    private final JComboBox<Resolution> comboBox3Res;
    private final JComboBox<Maps> comboBox1Maps;
    private final JComboBox<Maps> comboBox2Maps;
    private final JComboBox<Maps> comboBox3Maps;
    private final JComboBox<HUD> comboBox1Hud;
    private final JComboBox<HUD> comboBox2Hud;
    private final JComboBox<HUD> comboBox3Hud;
    private final JLabel label1Install;
    private final JLabel label2Install;
    private final JLabel label3Install;
    private final JLabel label1Patch;
    private final JLabel label2Patch;
    private final JLabel label3Patch;
    private final JLabel label1Running;
    private final JLabel label2Running;
    private final JLabel label3Running;
    private final ResourceBundle labels;
    private final ResourceBundle properties;
    private final JProgressBar loadingBar;
    private final JLabel loadingText;
    private final JButton btnApply;
    public final List<Boolean> patchLabels = new ArrayList<>(Collections.nCopies(3, false));
    private Controller controller;

    public View(JLabel titleBFME1, JLabel titleBFME2, JLabel titleBFME3, JLabel title1Res, JLabel title2Res, JLabel title3Res,
                JLabel title1Maps, JLabel title2Maps, JLabel title3Maps, JLabel title1Hud, JLabel title2Hud, JLabel title3Hud,
                JComboBox<Resolution> comboBox1Res, JComboBox<Resolution> comboBox2Res, JComboBox<Resolution> comboBox3Res,
                JComboBox<Maps> comboBox1Maps, JComboBox<Maps> comboBox2Maps, JComboBox<Maps> comboBox3Maps,
                JComboBox<HUD> comboBox1Hud, JComboBox<HUD> comboBox2Hud, JComboBox<HUD> comboBox3Hud,
                JLabel label1Install, JLabel label2Install, JLabel label3Install, JLabel label1Patch, JLabel label2Patch, JLabel label3Patch,
                JLabel label1Running, JLabel label2Running, JLabel label3Running,
                ResourceBundle properties, ResourceBundle labels, JProgressBar loadingBar, JLabel loadingText, JButton btnApply) {

        this.titleBFME1 = titleBFME1;
        this.titleBFME2 = titleBFME2;
        this.titleBFME3 = titleBFME3;

        this.title1Res = title1Res;
        this.title2Res = title2Res;
        this.title3Res = title3Res;

        this.title1Maps = title1Maps;
        this.title2Maps = title2Maps;
        this.title3Maps = title3Maps;

        this.title1Hud = title1Hud;
        this.title2Hud = title2Hud;
        this.title3Hud = title3Hud;

        this.comboBox1Res = comboBox1Res;
        this.comboBox2Res = comboBox2Res;
        this.comboBox3Res = comboBox3Res;

        this.comboBox1Maps = comboBox1Maps;
        this.comboBox2Maps = comboBox2Maps;
        this.comboBox3Maps = comboBox3Maps;

        this.comboBox1Hud = comboBox1Hud;
        this.comboBox2Hud = comboBox2Hud;
        this.comboBox3Hud = comboBox3Hud;

        this.label1Install = label1Install;
        this.label2Install = label2Install;
        this.label3Install = label3Install;

        this.label1Patch = label1Patch;
        this.label2Patch = label2Patch;
        this.label3Patch = label3Patch;

        this.label1Running = label1Running;
        this.label2Running = label2Running;
        this.label3Running = label3Running;

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
        switch (gameID) {
            case BFME1: renderGame(controller.getGame(gameID), titleBFME1,
                    title1Res, title1Maps, title1Hud, label1Install, label1Patch, label1Running,
                    comboBox1Res, comboBox1Maps, comboBox1Hud, display); break;
            case BFME2: renderGame(controller.getGame(gameID), titleBFME2,
                    title2Res, title2Maps, title2Hud, label2Install, label2Patch, label2Running,
                    comboBox2Res, comboBox2Maps, comboBox2Hud, display); break;
            case ROTWK: renderGame(controller.getGame(gameID), titleBFME3,
                    title3Res, title3Maps, title3Hud, label3Install, label3Patch, label3Running,
                    comboBox3Res, comboBox3Maps, comboBox3Hud, display); break;
        }
    }

    private void renderGame(Game game, JLabel title, JLabel resTitle, JLabel mapsTitle, JLabel hudTitle, JLabel installLabel, JLabel patchLabel, JLabel runningLabel, JComboBox<Resolution> resComboBox, JComboBox<Maps> mapsComboBox, JComboBox<HUD> hudComboBox, Display display) {
        if (game == null)
            return;

        boolean installed = game.isInstalled();
        title.setEnabled(installed);
        resTitle.setEnabled(installed);
        mapsTitle.setEnabled(installed);
        hudTitle.setEnabled(installed);

        setInstallationLabel(game, installLabel, patchLabel);
        setRunningLabel(game, runningLabel);
        setComboBoxResolution(game, resComboBox, labels, display);
        setComboBoxMaps(game, mapsComboBox, labels);
        setComboBoxHud(game, hudComboBox, labels);
    }

    private void setRunningLabel(Game game, JLabel runningLabel) {
        runningLabel.setText(null);
        runningLabel.setEnabled(game.isRunning());

        if (game.isRunning())
            runningLabel.setText(getRunningDisclaimer());
    }

    private void setComboBoxHud(Game game, JComboBox<HUD> comboBox, ResourceBundle labels) {
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

    private void setComboBoxMaps(Game game, JComboBox<Maps> comboBox, ResourceBundle labels) {
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

    private void setComboBoxResolution(Game game, JComboBox<Resolution> comboBox, ResourceBundle labels, Display display) {
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

    private void setInstallationLabel(Game game, JLabel labelInfo, JLabel labelPatch) {
        labelInfo.setEnabled(game.isInstalled());
        labelPatch.setText(null);
        labelPatch.setCursor(Cursor.getDefaultCursor());
        labelPatch.setEnabled(!game.isRunning());

        int index = game.getId().ordinal();
        MouseListener[] listeners = labelPatch.getMouseListeners();
        for (MouseListener l : listeners)
            labelPatch.removeMouseListener(l);
        patchLabels.set(index, false);

        if (!game.isInstalled())
            labelInfo.setText(labels.getString("game.notInstalled"));

        if (game.isInstalled() && game.isPatched())
            labelInfo.setText(getVersionLabel(game, ""));

        if (game.isInstalled() && !game.isPatched()) {
            labelInfo.setText(getVersionLabel(game, " -- "));
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

    private String getVersionLabel(Game game, String extend) {
        return "<html><font color='green'>" + labels.getString("game.installed") + game.getVersionInstalled() + "</font>" + extend + "</html>";
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