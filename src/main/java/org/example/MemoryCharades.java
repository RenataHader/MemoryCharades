package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class MemoryCharades extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Welcome to Memory Charades!");
        Scene scene = new Scene(label, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Memory Charades");
        stage.show();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            System.out.println("Połączono z bazą danych!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
