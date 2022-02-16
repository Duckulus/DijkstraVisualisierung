package de.amin.dijkstrafacharbeit.gui;

import de.amin.dijkstrafacharbeit.data.Edge;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EdgeLine extends Line {

    private VertexPane from;
    private VertexPane to;
    private double weight;

    public EdgeLine(VertexPane startDot, VertexPane endDot, Pane pane,Double weight) {
        this.from = startDot;
        this.to = endDot;
        this.weight = weight;

        Line line = this;
        Label label = new Label();
        label.setText(String.valueOf(weight));
        label.setStyle("-fx-font-size: 20");
        pane.getChildren().add(label);
        label.layoutXProperty().bind(line.endXProperty().subtract(line.endXProperty().subtract(line.startXProperty()).divide(2)));
        label.layoutYProperty().bind(line.endYProperty().subtract(line.endYProperty().subtract(line.startYProperty()).divide(2)));
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        line.startXProperty().bind(startDot.layoutXProperty().add(startDot.translateXProperty()).add(startDot.widthProperty().divide(2)));
        line.startYProperty().bind(startDot.layoutYProperty().add(startDot.translateYProperty()).add(startDot.heightProperty().divide(2)));
        line.endXProperty().bind(endDot.layoutXProperty().add(endDot.translateXProperty()).add(endDot.widthProperty().divide(2)));
        line.endYProperty().bind(endDot.layoutYProperty().add(endDot.translateYProperty()).add(endDot.heightProperty().divide(2)));
    }

    public VertexPane getFrom() {
        return from;
    }

    public VertexPane getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }
}
