package org.example;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Game implements Serializable {
    protected List<Player> players;
    protected boolean isGameStarted;

    public Game() {
        this.players = new ArrayList<>();
        this.isGameStarted = false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void addPlayer(String playerName) throws Exception {
        if (playerName == null || playerName.isEmpty()) {
            throw new Exception("Nazwa gracza nie może być pusta.");
        }
        players.add(new Player(playerName));
    }

    public abstract void startGame();

    public abstract String getGameName();

    public Player getWinner() {
        Player winner = null;
        int maxScore = -1;

        for (Player p : players) {
            if (p.getScore() > maxScore) {
                maxScore = p.getScore();
                winner = p;
            }
        }
        return winner;
    }
}
