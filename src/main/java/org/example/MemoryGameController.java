package org.example;

public class MemoryGameController {
    private final MemoryGame game;

    public MemoryGameController(MemoryGame game) {
        this.game = game;
    }

    public boolean isPairMatched(int row1, int col1, int row2, int col2) {
        MemoryGame.Card first = game.getBoard()[row1][col1];
        MemoryGame.Card second = game.getBoard()[row2][col2];

        if (first.isMatched() || second.isMatched()) return false;

        if (first.getId() == second.getId()) {
            first.setMatched(true);
            second.setMatched(true);
            game.incrementMatchedPairs();
            game.getPlayers().get(game.getCurrentPlayerIndex()).increaseScore(1);
            return true;
        } else {
            nextPlayer();
            return false;
        }
    }

    public void nextPlayer() {
        int next = (game.getCurrentPlayerIndex() + 1) % game.getPlayers().size();
        game.setCurrentPlayerIndex(next);
    }

    public boolean isGameOver() {
        int totalPairs = (game.getRows() * game.getCols()) / 2;
        return game.getMatchedPairs() >= totalPairs;
    }

    public boolean canFlipCard(int row, int col) {
        return !game.getBoard()[row][col].isMatched() && !game.getBoard()[row][col].isRevealed();
    }

    public void revealCard(int row, int col) {
        game.getBoard()[row][col].setRevealed(true);
    }

    public void hideCard(int row, int col) {
        game.getBoard()[row][col].setRevealed(false);
    }

    public int getCardId(int row, int col) {
        return game.getBoard()[row][col].getId();
    }

    public boolean isCardMatched(int row, int col) {
        return game.getBoard()[row][col].isMatched();
    }

    public MemoryGame getGame() {
        return game;
    }
}
