package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private final int port;
    private final Map<String, List<GameRoom>> activeRooms;
    private final ExecutorService threadPool;

    public GameServer(int port) {
        this.port = port;
        this.activeRooms = new HashMap<>();
        this.threadPool = Executors.newCachedThreadPool(); // lub newFixedThreadPool(n)
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Serwer uruchomiony na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Nowe połączenie: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, this);
                threadPool.submit(handler); // zamiast new Thread(...)
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Błąd serwera: " + e.getMessage());
        } finally {
            shutdownExecutor();
        }
    }

    private void shutdownExecutor() {
        threadPool.shutdown();
        System.out.println("[SERVER] Zamykanie puli wątków...");
        try {
            if (!threadPool.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                System.err.println("[SERVER] Timeout przy zamykaniu puli wątków.");
            }
        } catch (InterruptedException e) {
            System.err.println("[SERVER] Przerwano zamykanie puli wątków.");
            Thread.currentThread().interrupt();
        }
    }

    public synchronized GameRoom assignPlayerToRoom(String gameType, Game gameInstance, int maxPlayers, ClientHandler clientHandler) {
        List<GameRoom> rooms = activeRooms.computeIfAbsent(gameType, k -> new ArrayList<>());

        for (GameRoom room : rooms) {
            if (!room.isStarted() && room.getCurrentPlayerCount() < room.getMaxPlayers()) {
                if (room.addClient(clientHandler)) {
                    return room;
                }
            }
        }

        String roomId = gameType + "#" + (rooms.size() + 1);
        GameRoom newRoom = new GameRoom(roomId, gameInstance, maxPlayers);
        newRoom.addClient(clientHandler);
        rooms.add(newRoom);
        System.out.println("[SERVER] Utworzono nowy pokój: " + roomId);
        return newRoom;
    }

    public static void main(String[] args) {
        GameServer server = new GameServer(12345);
        server.start();
    }
}