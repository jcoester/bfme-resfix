package view;

import controller.Controller;
import helper.SoundPlayer;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static model.GameID.*;

public class MainView extends JFrame {
    private final EnumMap<GameID, GameView> gameViews;
    private final ResourceBundle labels;
    private final ResourceBundle properties;
    private GameView gameView1;
    private GameView gameView2;
    private GameView gameView3;
    private JButton button;
    private JLabel progressText;
    private JProgressBar progressBar;
    private JPanel root;

    public MainView(ResourceBundle properties, ResourceBundle labels) {
        this.labels = labels;
        this.properties = properties;
        
        SwingUtilities.invokeLater(() -> {
            this.setContentPane(root);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setIconImage(new ImageIcon(Objects.requireNonNull(MainView.class.getResource("/icon/icon-11.png"))).getImage());
            this.setTitle(properties.getString("appName") + " v" + properties.getString("version"));

            this.progressText.setText("\u00A0"); // Empty progressText to keep occupy space

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button.isEnabled()) SoundPlayer.playSound("button", "Hover.wav", "mouseEntered");
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (button.isEnabled()) SoundPlayer.playSound("button", "Press.wav", "mouseClicked");
                }
            });
        });

        // Initialize gameViews map
        gameViews = new EnumMap<>(GameID.class);
        gameViews.put(BFME1, gameView1);
        gameViews.put(BFME2, gameView2);
        gameViews.put(ROTWK, gameView3);
    }

    public void setController(Controller controller) {
        gameViews.get(BFME1).passRefs(labels, properties, this, controller);
        gameViews.get(BFME2).passRefs(labels, properties, this, controller);
        gameViews.get(ROTWK).passRefs(labels, properties, this, controller);
    }

    public void applyResize() {
        setMinimumSize(new Dimension(0,0));
        pack();
        setMinimumSize(getSize());
    }

    public void addButtonListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public void renderGame(Game game, Display display) {
        gameViews.get(game.getId()).render(game, display);
    }

    public void updateGameTitlesFont(Font ringBearer, Color gold) {
        gameViews.get(BFME1).updateTitleFont(labels.getString("title.bfme1"), ringBearer, gold);
        gameViews.get(BFME2).updateTitleFont(labels.getString("title.bfme2"), ringBearer, gold);
        gameViews.get(ROTWK).updateTitleFont(labels.getString("title.rotwk"), ringBearer, gold);
    }

    public JLabel getProgressText() {
        return progressText;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public GameView getGameView(GameID gameId) {
        return gameViews.get(gameId);
    }

    public void disableUI() {
        button.setEnabled(false);
        progressBar.setIndeterminate(true);
    }

    public void enableUI() {
        button.setEnabled(true);
        progressBar.setIndeterminate(false);
    }

    public void resetProgress() {
        progressText.setText("\u00A0");
        progressBar.setValue(0);
    }

    public void renderResBox(Game game, Display display) {
        gameViews.get(game.getId()).renderResBox(game, display);
    }

    public void renderMapsBox(Game game) {
        gameViews.get(game.getId()).renderMapsBox(game);
    }

    public void renderHudBox(Game game) {
        gameViews.get(game.getId()).renderHudBox(game);
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