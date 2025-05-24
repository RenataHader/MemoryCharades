package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MemorySettingsController {

    @FXML private TextField nicknameField;
    @FXML private ToggleGroup difficultyGroup;
    @FXML private CheckBox timerCheckbox;
    @FXML private ComboBox<Integer> playersComboBox;  // zmienione z ToggleGroup
    @FXML private ImageView backgroundImage;

    @FXML
    public void initialize() {
        // Skalowanie tła
        backgroundImage.fitWidthProperty().bind(((StackPane) backgroundImage.getParent()).widthProperty());
        backgroundImage.fitHeightProperty().bind(((StackPane) backgroundImage.getParent()).heightProperty());

        // Ustaw domyślną wartość ComboBox
        playersComboBox.getSelectionModel().selectFirst(); // Wybierze pierwszą opcję (2 graczy)
    }

    @FXML
    private void handleStartButton() {
        String nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            showAlert("Wprowadź nickname!");
            nicknameField.requestFocus();
            return;
        }

        if (difficultyGroup.getSelectedToggle() == null) {
            showAlert("Wybierz rozmiar planszy!");
            return;
        }

        if (playersComboBox.getValue() == null) {
            showAlert("Wybierz liczbę graczy!");
            playersComboBox.requestFocus();
            return;
        }

        int cardCount = Integer.parseInt(difficultyGroup.getSelectedToggle().getUserData().toString());
        int players = playersComboBox.getValue();
        boolean timerEnabled = timerCheckbox.isSelected();

        // 4.5 Stwórz gracza
        Player currentPlayer = new Player(nickname);
        System.out.println("Utworzono gracza: " + currentPlayer.getNickname() + " (ID: " + currentPlayer.getPlayerId() + ")");

        // 5. Przejdź do gry
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/MemoryGameView.fxml"));
            Parent gameView = loader.load();

            // Możesz tu przekazać gracza i ustawienia do kontrolera gry:
            // MemoryGameController controller = loader.getController();
            // controller.initData(currentPlayer, cardCount, players, timerEnabled);

            StackPane root = (StackPane) nicknameField.getScene().getRoot();
            root.getChildren().setAll(gameView);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Błąd podczas ładowania gry: " + e.getMessage());
        }
    }


    @FXML
    private void handleBackButton() throws IOException {
        Parent menuView = FXMLLoader.load(getClass().getResource("/org/example/GameSelectionView.fxml"));
        StackPane parent = (StackPane) nicknameField.getScene().getRoot();
        parent.getChildren().setAll(menuView);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uwaga");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
