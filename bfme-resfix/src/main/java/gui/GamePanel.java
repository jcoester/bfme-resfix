package gui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
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
}



