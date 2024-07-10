package gui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameID id;
    private JComboBox<Resolution> comboBoxRes;
    private JComboBox<Maps> comboBoxMaps;
    private JComboBox<HUD> comboBoxHud;
    private JLabel labelRunning;
    private JLabel labelInstall;
    private JLabel labelPatch;
    private JLabel titleBFME;
    private JLabel titleRes;
    private JLabel titleMaps;
    private JLabel titleHud;
    private JLabel labelSpacer;
    private JPanel gamePanel;

    public GamePanel() {

    }

    public void updatePanel(Game game, Display display) {

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



