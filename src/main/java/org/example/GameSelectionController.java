package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class GameSelectionController {

    @FXML
    private StackPane mainContainer;

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
    private void handleMemoryButton() throws IOException {
        Parent memoryView = FXMLLoader.load(getClass().getResource("/org/example/MemorySettingsView.fxml"));
        mainContainer.getChildren().setAll(memoryView);
    }

    @FXML
    private void handleCharadesButton() throws IOException {
        Parent charadesView = FXMLLoader.load(getClass().getResource("/org/example/CharadesGameView.fxml"));
        mainContainer.getChildren().setAll(charadesView);
    }
}

