package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;

public class GameSelectionController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    public void initialize() {
        backgroundImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
                backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
            }
        });
    }


    @FXML
    private void handleMemoryButton(ActionEvent event) throws IOException {
        loadGameView("/org/example/MemoryGameView.fxml", "Memory Game", event);
    }

    @FXML
    private void handleCharadesButton(ActionEvent event) throws IOException {
        loadGameView("/org/example/CharadesGameView.fxml", "Charades Game", event);
    }

    private void loadGameView(String fxmlPath, String title, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = new Stage();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.setMaximized(true);
        stage.show();

        // Zamknięcie obecnego okna
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}