package de.amin.dijkstrafacharbeit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

public class DijkstraController {

    @FXML
    private Canvas canvas;

    public void onStartButtonClick(ActionEvent actionEvent) {
        System.out.println("Start button clicked");
        canvas.getGraphicsContext2D().rect(1,1, 20, 20);
    }
}