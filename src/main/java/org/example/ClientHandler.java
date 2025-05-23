package org.example;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Player player;
    private GameRoom assignedRoom;

    public ClientHandler(Socket socket, GameServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            // 1. Odbierz nick i typ gry od klienta
            String nickname = (String) in.readObject();
            String gameType = (String) in.readObject();
            int requestedPlayers = in.readInt();

            this.player = new Player(nickname);
            System.out.println("[+] Gracz " + nickname + " wybrał grę: " + gameType);

            // 2. Przydziel pokój
            Game game;
            if (gameType.equalsIgnoreCase("Charades")) {
                game = new CharadesGame(requestedPlayers);
            } else if (gameType.equalsIgnoreCase("Memory")) {
                game = new MemoryGame();
            } else {
                sendMessage("Nieznany typ gry: " + gameType);
                close();
                return;
            }

            this.assignedRoom = server.assignPlayerToRoom(gameType, game, requestedPlayers, this);

            if (assignedRoom != null) {
                sendMessage("Dołączono do pokoju: " + assignedRoom.getRoomId());
            } else {
                sendMessage("Nie można dołączyć do pokoju.");
            }

        } catch (Exception e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        } finally {
            close();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Błąd przy wysyłaniu wiadomości: " + e.getMessage());
        }
    }

    public Player getPlayer() {
        return player;
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
