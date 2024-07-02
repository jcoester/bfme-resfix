package controller;

import model.*;
import view.View;

import javax.swing.*;
import java.util.List;

public class Controller {
    private final List<Game> games;
    private Display display;
    private final View view;

    public Controller(List<Game> games, Display display, View view) {
        this.games = games;
        this.display = display;
        this.view = view;
    }

    public void updateGame(Game game) {
        this.games.set(game.getId().ordinal(), game);
    }

    public void updateDisplay(Display display) {
        this.display = display;
    }

    public void updateView() {
        SwingUtilities.invokeLater(() -> view.render(display));
    }

    public void updateGameView(GameID gameID) {
        SwingUtilities.invokeLater(() -> view.renderGame(gameID, display));
    }

    public Game getGame(GameID gameId) {
        for (Game game : games) {
            if (game.getId() == gameId)
                return game;
        }
        return null;
    }

    public List<Game> getGames() {
        return games;
    }

    public Display getDisplay() {
        return display;
    }
}
