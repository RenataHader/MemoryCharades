package org.example;

import java.io.*;
import java.net.Socket;

public class GameClient {
    private final String host;
    private final int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(String nickname, String gameType, int playerCount) {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("[CLIENT] Połączono z serwerem.");

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // 1. Wyślij dane
            out.writeObject(nickname);
            out.writeObject(gameType);
            out.writeInt(playerCount);
            out.flush();

            // 2. Czekaj na wiadomości
            while (true) {
                try {
                    Object msg = in.readObject();
                    if (msg instanceof String str) {
                        System.out.println("[SERVER]: " + str);
                    }
                } catch (EOFException e) {
                    System.out.println("[CLIENT] Serwer zakończył połączenie.");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[CLIENT] Błąd: " + e.getMessage());
        }
    }
}
