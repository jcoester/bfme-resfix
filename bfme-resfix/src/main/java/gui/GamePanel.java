package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
    private GameID gameID;
    public JComboBox<Resolution> comboBoxRes;
    public JComboBox<Maps> comboBoxMaps;
    public JComboBox<HUD> comboBoxHud;
    public JLabel labelRunning;
    public JLabel labelInstall;
    public JLabel labelSpacer;
    public JLabel labelPatch;
    public JLabel titleBFME;
    public JPanel gamePanel;
    public JLabel titleRes;
    public JLabel titleMaps;
    public JLabel titleHud;

    public GamePanel() {

    }

    public void updateTitleFont(String text, Font ringBearer, Color gold) {
        titleBFME.setText(text);
        titleBFME.setFont(ringBearer);
        titleBFME.setForeground(gold);
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

    public void enableTitleBFME(boolean enabled) {
        titleBFME.setEnabled(enabled);
    }

    public void enableTitleRes(boolean enabled) {
        titleRes.setEnabled(enabled);
    }

    public void enableTitleMaps(boolean enabled) {
        titleMaps.setEnabled(enabled);
    }

    public void enableTitleHud(boolean enabled) {
        titleHud.setEnabled(enabled);
    }

    public void enableInstall(boolean enabled) {
        labelInstall.setEnabled(enabled);
    }

    public void showSpacer(boolean visible) {
        labelSpacer.setVisible(visible);
    }

    public void enablePatch(boolean enabled) {
        labelPatch.setEnabled(enabled);
    }

    public void updatePatch(String text) {
        labelPatch.setText(text);
    }

    public void updatePatchCursor(Cursor cursor) {
        labelPatch.setCursor(cursor);
    }

    public void showPatch(boolean visible) {
        labelPatch.setVisible(visible);
    }

    public MouseListener[] getPatchMouseListeners() {
        return labelPatch.getMouseListeners();
    }

    public void removePatchMouseListener(MouseListener l) {
        labelPatch.removeMouseListener(l);
    }

    public void updateInstall(String text) {
        labelInstall.setText(text);
    }

    public JComboBox<Resolution> getJComboBoxRes() {
        return comboBoxRes;
    }
}



