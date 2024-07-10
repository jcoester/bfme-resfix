package gui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameID gameID;
    private JComboBox<Resolution> comboBoxRes;
    private JComboBox<Maps> comboBoxMaps;
    private JComboBox<HUD> comboBoxHud;
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

    public void setInstallEnabled(boolean enabled) {
        labelInstall.setEnabled(enabled);
    }

    public void setPatchEnabled(boolean enabled) {
        labelPatch.setEnabled(enabled);
    }

    public void setPatchText(String text) {
        labelPatch.setText(text);
    }

    public void setPatchCursor(Cursor cursor) {
        labelPatch.setCursor(cursor);
    }

    public void updateTitleText(String text) {
        titleBFME.setText(text);
    }

    public void updatePatchText(String text) {
        labelPatch.setText(text);
    }
}



