package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MemoryGameView {
    @FXML
    private GridPane cardGrid;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label playerLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label userLabel;


    private final MemoryGame game = new MemoryGame();
    private Image cardBackImage;

    int rows = game.getRows();
    int cols = game.getCols();

    @FXML
    public void initialize() {
        backgroundImage.setImage(new Image(getClass().getResource("/images/table.png").toExternalForm()));
        cardBackImage = new Image(getClass().getResource("/images/card_back.png").toExternalForm());

        javafx.application.Platform.runLater(() -> {
            backgroundImage.fitWidthProperty().bind(backgroundImage.getScene().widthProperty());
            backgroundImage.fitHeightProperty().bind(backgroundImage.getScene().heightProperty());

            double sceneWidth = cardGrid.getScene().getWidth();
            double sceneHeight = cardGrid.getScene().getHeight();

            double lewyPanel = 300;
            double prawyMargines = 200;
            double gornyMargines = 100;
            double dolnyPanel = 150;

            double dostepnaSzerokosc = sceneWidth - lewyPanel - prawyMargines - ((cols - 1)*20);
            double dostepnaWysokosc = sceneHeight - gornyMargines - dolnyPanel - ((rows - 1)*20);

            int gap = 20;

            int cardHeight = (int) ((dostepnaWysokosc - ((rows - 1) * gap)) / rows);
            int cardWidth = (int) ((dostepnaSzerokosc - ((cols - 1) * gap)) / cols);

            if(cardWidth>120)
                cardWidth = 120;
            if(cardHeight>180)
                cardHeight = 180;

            System.out.println(cardHeight);
            System.out.println(cardWidth);

            game.startGame();
            cardGrid.getChildren().clear();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Button cardButton = new Button();
                    ImageView cardView = new ImageView(cardBackImage);
                    cardView.setFitWidth(cardWidth);
                    cardView.setFitHeight(cardHeight);
                    cardView.setPreserveRatio(false);

                    cardButton.setGraphic(cardView);
                    cardButton.setStyle("-fx-background-color: transparent;");
                    cardButton.setPrefSize(cardWidth, cardHeight);
                    cardGrid.add(cardButton, j, i);
                }
            }
            cardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        });
    }
}
