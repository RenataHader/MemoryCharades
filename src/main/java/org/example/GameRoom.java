package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private final String roomId;
    private final Game gameInstance;
    private final List<ClientHandler> clients;
    private final int maxPlayers;
    private boolean started;

    public GameRoom(String roomId, Game gameInstance, int maxPlayers) {
        this.roomId = roomId;
        this.gameInstance = gameInstance;
        this.clients = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.started = false;
    }

    public synchronized boolean addClient(ClientHandler clientHandler) {
        if (clients.size() >= maxPlayers || started) {
            return false;
        }

        clients.add(clientHandler);
        gameInstance.getPlayers().add(clientHandler.getPlayer());

        if (clients.size() == maxPlayers) {
            startGame();
        }

        return true;
    }

    private void startGame() {
        started = true;
        gameInstance.startGame();

        broadcastMessage("Gra w pokoju " + roomId + " rozpoczyna się!");

        // Możesz dodać logikę rozpoczęcia rozgrywki z klientami
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public Game getGameInstance() {
        return gameInstance;
    }

    public boolean isStarted() {
        return started;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrentPlayerCount() {
        return clients.size();
    }
}