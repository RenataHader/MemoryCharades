package org.example;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.RotateTransition;
import javafx.util.Duration;


public class MemoryGameView {

    @FXML private GridPane cardGrid;
    @FXML private ImageView backgroundImage;
    @FXML private Label playerLabel;
    @FXML private Label scoreLabel;
    @FXML private Label userLabel;

    private final MemoryGame game = new MemoryGame();
    private final MemoryGameController controller = new MemoryGameController(game);
    private Button firstCardButton = null;
    private Button secondCardButton = null;
    private ImageView firstCardView = null;
    private ImageView secondCardView = null;
    private int firstRow = -1, firstCol = -1;
    private boolean isProcessing = false;

    private Image cardBackImage;

    int rows = game.getRows();
    int cols = game.getCols();

    private void flipCard(Button cardButton, ImageView cardView, Image backImage, Image frontImage) {
        RotateTransition flipOut = new RotateTransition(Duration.millis(200), cardView);
        flipOut.setFromAngle(0);
        flipOut.setToAngle(90);

        RotateTransition flipIn = new RotateTransition(Duration.millis(200), cardView);
        flipIn.setFromAngle(270);
        flipIn.setToAngle(360);

        flipOut.setOnFinished(event -> {
            cardView.setImage(frontImage);
            flipIn.play();
        });

        flipOut.play();
    }

    private void resetSelection() {
        firstCardButton = null;
        secondCardButton = null;
        firstCardView = null;
        secondCardView = null;
        firstRow = -1;
        firstCol = -1;
    }

    private Image frontImageFromId(int id) {
        return new Image(getClass().getResource("/images/card_" + id + ".png").toExternalForm());
    }


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
            game.printBoard();

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

                    int row = i;
                    int col = j;

                    cardButton.setOnAction(e -> {
                        if (isProcessing || !controller.canFlipCard(row, col)) return;

                        controller.revealCard(row, col);
                        Image frontImage = frontImageFromId(controller.getCardId(row, col));
                        flipCard(cardButton, cardView, cardBackImage, frontImage);

                        if (firstCardButton == null) {
                            firstCardButton = cardButton;
                            firstCardView = cardView;
                            firstRow = row;
                            firstCol = col;
                        } else if (secondCardButton == null) {
                            secondCardButton = cardButton;
                            secondCardView = cardView;
                            isProcessing = true;

                            int secondRow = row;
                            int secondCol = col;

                            boolean match = controller.isPairMatched(firstRow, firstCol, secondRow, secondCol);

                            if (match) {
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(ev -> {
                                    // Ukryj trafione przyciski
                                    firstCardButton.setVisible(false);
                                    secondCardButton.setVisible(false);

                                    resetSelection();
                                    isProcessing = false;

                                    if (controller.isGameOver()) {
                                        System.out.println("Gra zakończona!");
                                    }
                                });
                                pause.play();
                            } else {
                                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                                pause.setOnFinished(ev -> {
                                    controller.hideCard(firstRow, firstCol);
                                    controller.hideCard(secondRow, secondCol);
                                    flipCard(firstCardButton, firstCardView, frontImageFromId(controller.getCardId(firstRow, firstCol)), cardBackImage);
                                    flipCard(secondCardButton, secondCardView, frontImageFromId(controller.getCardId(secondRow, secondCol)), cardBackImage);
                                    controller.nextPlayer();
                                    resetSelection();
                                    isProcessing = false;
                                });
                                pause.play();
                            }
                        }
                    });



                    cardGrid.add(cardButton, j, i);
                }
            }

            cardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        });
    }
}
