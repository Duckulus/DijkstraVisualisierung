package de.amin.dijkstrafacharbeit.gui;

import de.amin.dijkstrafacharbeit.DijkstraController;
import de.amin.dijkstrafacharbeit.data.Vertex;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class VertexPane extends StackPane {

    private Vertex vertex;
    private Circle dot;
    /**
     * Credit https://stackoverflow.com/questions/53366602/creating-directed-edges-javafx
     */
    public VertexPane(String text) {
        super();
        this.vertex = new Vertex(text);
        double radius = 30;
        double paneSize = 2 * radius;
        StackPane dotPane = this;
        dot = new Circle();
        dot.setRadius(radius);
        dot.setStyle("-fx-fill:cyan;-fx-stroke-width:2px;-fx-stroke:black;");
        toFront();
        Label txt = new Label(text);
        txt.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        dotPane.getChildren().addAll(dot, txt);
        dotPane.setPrefSize(paneSize, paneSize);
        dotPane.setMaxSize(paneSize, paneSize);
        dotPane.setMinSize(paneSize, paneSize);
        dotPane.setOnMousePressed(e -> {
            DijkstraController.sceneX = e.getSceneX();
            DijkstraController.sceneY = e.getSceneY();
            DijkstraController.layoutX = dotPane.getLayoutX();
            DijkstraController.layoutY = dotPane.getLayoutY();
        });

        EventHandler<MouseEvent> dotOnMouseDraggedEventHandler = e -> {
            double offsetX = e.getSceneX() - DijkstraController.sceneX;
            double offsetY = e.getSceneY() - DijkstraController.sceneY;

            Bounds parentBounds = dotPane.getParent().getLayoutBounds();

            double currPaneLayoutX = dotPane.getLayoutX();
            double currPaneWidth = dotPane.getWidth();
            double currPaneLayoutY = dotPane.getLayoutY();
            double currPaneHeight = dotPane.getHeight();

            if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                dotPane.setTranslateX(offsetX);
            } else if (currPaneLayoutX + offsetX < 0) {
                dotPane.setTranslateX(-currPaneLayoutX);
            } else {
                dotPane.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
            }

            if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                dotPane.setTranslateY(offsetY);
            } else if (currPaneLayoutY + offsetY < 0) {
                dotPane.setTranslateY(-currPaneLayoutY);
            } else {
                dotPane.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
            }
        };
        dotPane.setOnMouseDragged(dotOnMouseDraggedEventHandler);
        dotPane.setOnMouseReleased(e -> {
            // Updating the new layout positions
            dotPane.setLayoutX(DijkstraController.layoutX + dotPane.getTranslateX());
            dotPane.setLayoutY(DijkstraController.layoutY + dotPane.getTranslateY());

            // Resetting the translate positions
            dotPane.setTranslateX(0);
            dotPane.setTranslateY(0);
        });

    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setColor(String color) {
        dot.setStyle("-fx-fill:" + color +";-fx-stroke-width:2px;-fx-stroke:black;");
    }
}
