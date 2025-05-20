package org.example;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;



public class CharadesGameView {

    @FXML private ImageView backgroundImage;
    @FXML private Canvas drawingCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private ToggleButton eraserToggle;
    @FXML private ToggleButton drawLine, drawRect, drawOval;
    @FXML private Slider sizeSlider;
    @FXML private Button clearButton;
    @FXML private Label timerLabel;
    @FXML private Button endButton;

    private double startX, startY;
    private Timeline timer;
    private int timeLeft = 60;



    private GraphicsContext gc;

    @FXML
    public void initialize() {
        // Tło
        backgroundImage.setImage(new Image(getClass().getResource("/images/table.png").toExternalForm()));
        javafx.application.Platform.runLater(() -> {
            backgroundImage.fitWidthProperty().bind(backgroundImage.getScene().widthProperty());
            backgroundImage.fitHeightProperty().bind(backgroundImage.getScene().heightProperty());
        });

        ToggleGroup drawModes = new ToggleGroup();
        drawLine.setToggleGroup(drawModes);
        drawRect.setToggleGroup(drawModes);
        drawOval.setToggleGroup(drawModes);

        drawLine.setOnAction(e -> updateToggleStyles());
        drawRect.setOnAction(e -> updateToggleStyles());
        drawOval.setOnAction(e -> updateToggleStyles());
        eraserToggle.setOnAction(e -> updateToggleStyles());

        endButton.setOnAction(e -> disableDrawingTools());

        // Rysowanie
        gc = drawingCanvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());

        colorPicker.setValue(Color.BLACK);

        // Obsługa rysowania
        drawingCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            startX = e.getX();
            startY = e.getY();
            gc.beginPath();
            gc.moveTo(startX, startY);
            gc.setStroke(eraserToggle.isSelected() ? Color.WHITE : colorPicker.getValue());
            gc.setLineWidth(sizeSlider.getValue());
        });

        drawingCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!drawLine.isSelected() && !drawRect.isSelected() && !drawOval.isSelected()) {
                gc.setLineWidth(sizeSlider.getValue());
                gc.setStroke(eraserToggle.isSelected() ? Color.WHITE : colorPicker.getValue());
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            }
        });

        clearButton.setOnAction(e -> {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());
        });

        drawingCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            double endX = e.getX();
            double endY = e.getY();
            gc.setStroke(eraserToggle.isSelected() ? Color.WHITE : colorPicker.getValue());

            if (drawLine.isSelected()) {
                gc.strokeLine(startX, startY, endX, endY);
            } else if (drawRect.isSelected()) {
                gc.strokeRect(Math.min(startX, endX), Math.min(startY, endY),
                        Math.abs(endX - startX), Math.abs(endY - startY));
            } else if (drawOval.isSelected()) {
                gc.strokeOval(Math.min(startX, endX), Math.min(startY, endY),
                        Math.abs(endX - startX), Math.abs(endY - startY));
            }
        });

        startTimer();

    }

    private void startTimer() {
        if (timer != null) timer.stop();

        timeLeft = 60;
        timerLabel.setText("Czas: " + timeLeft + "s");

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Czas: " + timeLeft + "s");

            if (timeLeft <= 0) {
                timer.stop();
                timerLabel.setText("Koniec czasu!");
                // Można tu np. wyłączyć rysowanie
            }

            if (timeLeft <= 10) {
                timerLabel.setTextFill(Color.WHITE);
            } else if (timeLeft <= 30) {
                timerLabel.setTextFill(Color.ORANGE);
            } else {
                timerLabel.setTextFill(Color.YELLOW);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateToggleStyles() {
        String activeStyle = "-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px;";
        String defaultStyle = "-fx-font-size: 20px; -fx-font-weight: bold;";

        ToggleButton[] buttons = {drawLine, drawRect, drawOval, eraserToggle};

        for (ToggleButton btn : buttons) {
            if (btn.isSelected()) {
                btn.setStyle(activeStyle);
            } else {
                btn.setStyle(defaultStyle);
            }
        }
    }

    private void disableDrawingTools() {
        colorPicker.setDisable(true);
        drawLine.setDisable(true);
        drawRect.setDisable(true);
        drawOval.setDisable(true);
        eraserToggle.setDisable(true);
        sizeSlider.setDisable(true);
        clearButton.setDisable(true);
        endButton.setDisable(true); // opcjonalnie blokujemy sam "Zakończ"
    }


}
