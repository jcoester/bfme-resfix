package gui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
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

    public JComboBox<Resolution> getJComboBoxRes() {
        return comboBoxRes;
    }

    public JComboBox<Maps> getJComboBoxMaps() {
        return comboBoxMaps;
    }

    public JComboBox<HUD> getJComboBoxHud() {
        return comboBoxHud;
    }

    public JLabel getLabelRunning() {
        return labelRunning;
    }

    public JLabel getTitleBFME() {
        return titleBFME;
    }

    public JLabel getTitleRes() {
        return titleRes;
    }

    public JLabel getTitleMaps() {
        return titleMaps;
    }

    public JLabel getTitleHud() {
        return titleHud;
    }

    public JLabel getLabelInstall() {
        return labelInstall;
    }

    public JLabel getLabelSpacer() {
        return labelSpacer;
    }

    public JLabel getLabelPatch() {
        return labelPatch;
    }
}



