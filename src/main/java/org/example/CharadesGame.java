package org.example;

import java.io.*;
import java.util.*;

public class CharadesGame extends Game {
    private List<String> phrases;
    private Map<Player, String> currentPhrases;
    private int totalRounds;
    private int currentRound;
    private boolean gameOver;

    public CharadesGame(int totalRounds) throws IOException {
        super();
        this.phrases = loadRandomPhrasesFromFile(totalRounds);
        this.totalRounds = totalRounds;
        this.currentRound = 0;
        this.currentPhrases = new HashMap<>();
        this.gameOver = false;
    }

    @Override
    public void startGame() {
        if (players.size() < 2) {
            System.out.println("Nie mozna rozpoczac gry. Wymaganych co najmniej 2 graczy.");
            return;
        }
        isGameStarted = true;
        currentRound = 0;
    }

    @Override
    public String getGameName() {
        return "Charades";
    }

    private List<String> loadRandomPhrasesFromFile(int rounds) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("words.txt");
        if (in == null) {
            throw new FileNotFoundException("Nie znaleziono pliku: words.txt w katalogu resources.");
        }

        List<String> allLines = new BufferedReader(new InputStreamReader(in))
                .lines().toList();

        int totalNeeded = rounds * players.size();
        if (allLines.size() < totalNeeded) {
            throw new IllegalArgumentException("Za malo hasel. Potrzeba: " + totalNeeded);
        }

        Collections.shuffle(allLines);
        return allLines.subList(0, totalNeeded);
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public Map<Player, String> getCurrentPhrases() {
        return currentPhrases;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void incrementRound() {
        currentRound++;
        if (currentRound > totalRounds) {
            gameOver = true;
        }
    }

    public void setCurrentPhrases(Map<Player, String> currentPhrases) {
        this.currentPhrases = currentPhrases;
    }
}
