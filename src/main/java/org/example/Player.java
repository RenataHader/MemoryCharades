package org.example;

public class Player {
    private String nickname;
    private int score;
    private boolean isReady;
    private String playerId;

    public Player(String nickname) {
        this.nickname = nickname;
        this.score = 0;
        this.isReady = false;
        this.playerId = generateUniqueId();
    }

    public int getScore() {
        return score;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getPlayerId() {
        return playerId;
    }

    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

    public void increaseScore(int points) {
        this.score += points;
    }

    @Override
    public String toString() {
        return nickname + " (score: " + score + ")";
    }
}
