package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CharadesSettingsController {

    @FXML private TextField nicknameField;
    @FXML private ComboBox<Integer> playersComboBox;
    @FXML private ComboBox<Integer> roundsComboBox;
    @FXML private ImageView backgroundImage;

    @FXML
    public void initialize() {
        // Skalowanie tła
        backgroundImage.fitWidthProperty().bind(((StackPane) backgroundImage.getParent()).widthProperty());
        backgroundImage.fitHeightProperty().bind(((StackPane) backgroundImage.getParent()).heightProperty());

        // Domyślne wybory
        playersComboBox.getSelectionModel().selectFirst();
        roundsComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleStartButton() {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            showAlert("Wprowadź nickname!");
            nicknameField.requestFocus();
            return;
        }

        Integer players = playersComboBox.getValue();
        if (players == null) {
            showAlert("Wybierz liczbę graczy!");
            playersComboBox.requestFocus();
            return;
        }

        Integer rounds = roundsComboBox.getValue();
        if (rounds == null) {
            showAlert("Wybierz liczbę rund!");
            roundsComboBox.requestFocus();
            return;
        }

        // Tworzymy gracza jako lidera
        Player leader = new Player(nickname);
        System.out.println("Tworzę grę Charades:");
        System.out.println("Lider: " + leader.getNickname());
        System.out.println("Graczy: " + players);
        System.out.println("Rund: " + rounds);

        // Załaduj widok gry (placeholder - podmień na własny plik)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/CharadesGameView.fxml"));
            Parent gameView = loader.load();

            // Jeśli chcesz przekazać dane do kontrolera:
            // CharadesGameController controller = loader.getController();
            // controller.initGame(leader, players, rounds);

            StackPane root = (StackPane) nicknameField.getScene().getRoot();
            root.getChildren().setAll(gameView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Błąd ładowania widoku gry: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton() throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/GameSelectionView.fxml"));
        StackPane root = (StackPane) nicknameField.getScene().getRoot();
        root.getChildren().setAll(menuView);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uwaga");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
