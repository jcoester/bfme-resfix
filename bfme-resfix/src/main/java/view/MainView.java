package view;

import controller.Controller;
import helper.ExceptionHandler;
import helper.SoundPlayer;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static model.GameID.*;

public class MainView extends JFrame {
    private static final String HOVER_WAV = "Hover.wav";
    private static final String PRESS_WAV = "Press.wav";
    private final EnumMap<GameID, GameView> gameViews;
    private final ResourceBundle labels;
    private final ResourceBundle properties;
    private GameView gameView1;
    private GameView gameView2;
    private GameView gameView3;
    private JButton applyButton;
    private JLabel progressText;
    private JProgressBar progressBar;
    private JPanel root;
    private JButton helpButton;

    public MainView(ResourceBundle properties, ResourceBundle labels) {
        this.labels = labels;
        this.properties = properties;
        
        SwingUtilities.invokeLater(() -> {
            this.setContentPane(root);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setIconImage(new ImageIcon(Objects.requireNonNull(MainView.class.getResource("/icon/icon-11.png"))).getImage());
            this.setTitle(properties.getString("appName") + " v" + properties.getString("version"));

            this.progressText.setText("\u00A0"); // Empty progressText to keep occupy space

            addButtonListeners();
        });

        // Initialize gameViews map
        gameViews = new EnumMap<>(GameID.class);
        gameViews.put(BFME1, gameView1);
        gameViews.put(BFME2, gameView2);
        gameViews.put(ROTWK, gameView3);
    }

    private void addButtonListeners() {
        applyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (applyButton.isEnabled()) SoundPlayer.playSound(HOVER_WAV);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (applyButton.isEnabled()) SoundPlayer.playSound(PRESS_WAV);
            }
        });

        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                SoundPlayer.playSound(HOVER_WAV);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                SoundPlayer.playSound(PRESS_WAV);
                openHelp();
            }
        });
    }

    private static void openHelp() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new java.net.URI("https://github.com/jcoester/bfme-resfix"));
                } catch (Exception e) {
                    ExceptionHandler.getInstance().promptError("Error in openHelp()", e);
                }
            }
        } else {
            ExceptionHandler.getInstance().promptError("Desktop is not supported on this platform.", null);
        }
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
        applyButton.addActionListener(listener);
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
        applyButton.setEnabled(false);
        progressBar.setIndeterminate(true);
    }

    public void enableUI() {
        applyButton.setEnabled(true);
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

    public void renderDVDBox(Game game) {
        gameViews.get(game.getId()).renderDVDBox(game);
    }

    public void renderIntroBox(Game game) {
        gameViews.get(game.getId()).renderIntroBox(game);
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