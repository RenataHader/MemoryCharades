package org.example;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class CharadesGameView {

    @FXML private ImageView backgroundImage;
    @FXML private Canvas drawingCanvas;
    @FXML private ColorPicker colorPicker;
    @FXML private ToggleButton eraserToggle;
    @FXML private ToggleButton drawLine, drawRect, drawOval;
    private double startX, startY;
    @FXML private Slider sizeSlider;
    @FXML private Button clearButton;


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

        // Rysowanie
        gc = drawingCanvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, drawingCanvas.getWidth(), drawingCanvas.getHeight());

        colorPicker.setValue(Color.BLACK);

        eraserToggle.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                eraserToggle.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 20px;");
            } else {
                eraserToggle.setStyle("-fx-font-size: 20px;");
            }
        });

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

    }
}
