package gui;

import model.GameID;
import model.HUD;
import model.Maps;
import model.Resolution;

import javax.swing.*;

public class GameFrame extends JPanel {
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
}



