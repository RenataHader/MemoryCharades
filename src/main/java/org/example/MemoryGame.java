package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGame extends Game {
    private int rows = 3;
    private int cols = 8;
    private Card[][] board;
    private int matchedPairs;
    private int currentPlayerIndex;

    public MemoryGame() {
        super();
        this.board = new Card[rows][cols];
        this.matchedPairs = 0;
        this.currentPlayerIndex = 0;
        generateBoard();
    }

    @Override
    public void startGame() {
        if (players.size() < 1) {
            System.out.println("Nie mozna rozpoczac gry bez graczy.");
            return;
        }

        isGameStarted = true;
        matchedPairs = 0;
        currentPlayerIndex = 0;
        generateBoard();

        System.out.println("Gra Memory rozpoczeta! Graczy: " + players.size());
    }

    @Override
    public String getGameName() {
        return "Memory";
    }

    public Card[][] getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public int getMatchedPairs() {
        return matchedPairs;
    }

    public void incrementMatchedPairs() {
        matchedPairs++;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void generateBoard() {
        List<Integer> ids = new ArrayList<>();
        int totalPairs = (rows * cols) / 2;

        for (int i = 1; i <= totalPairs; i++) {
            ids.add(i);
            ids.add(i);
        }

        Collections.shuffle(ids);
        int index = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Card(ids.get(index++));
            }
        }
    }

    public class Card {
        private int id;
        private boolean isMatched;
        private boolean isRevealed;

        public Card(int id) {
            this.id = id;
            this.isMatched = false;
            this.isRevealed = false;
        }

        public int getId() {
            return id;
        }

        public boolean isMatched() {
            return isMatched;
        }

        public void setMatched(boolean matched) {
            isMatched = matched;
        }

        public boolean isRevealed() {
            return isRevealed;
        }

        public void setRevealed(boolean revealed) {
            isRevealed = revealed;
        }
    }
}
