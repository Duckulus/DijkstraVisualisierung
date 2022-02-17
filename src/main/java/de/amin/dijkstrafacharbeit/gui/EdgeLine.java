package de.amin.dijkstrafacharbeit.gui;

import de.amin.dijkstrafacharbeit.data.Edge;
import de.amin.dijkstrafacharbeit.data.Vertex;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Diese Klasse repräsentiert eine Graphenkante in der Benutzeroberfläche
 * mithilfe einer {@link Line}
 * Außerdem speichert sie eine Instanz der Datenklasse {@link Edge} um die Verwaltung zu erleichtern
 *
 * @author Amin Haddou
 */
public class EdgeLine extends Line {

    private VertexPane from;
    private VertexPane to;
    private double weight;
    private Edge edge;

    /**
     * Credit https://stackoverflow.com/questions/53366602/creating-directed-edges-javafx
     */
    public EdgeLine(VertexPane startDot, VertexPane endDot, Pane pane,Double weight) {
        this.from = startDot;
        this.to = endDot;
        this.weight = weight;
        this.edge = new Edge(from.getVertex(), to.getVertex(), weight);

        toFront();
        Line line = this;
        Label label = new Label();
        label.setText(String.valueOf(weight));
        label.setStyle("-fx-font-size: 20");
        pane.getChildren().add(label);
        label.layoutXProperty().bind(line.endXProperty().subtract(line.endXProperty().subtract(line.startXProperty()).divide(2)));
        label.layoutYProperty().bind(line.endYProperty().subtract(line.endYProperty().subtract(line.startYProperty()).divide(2)));
        line.setStroke(Color.BLACK);
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

    public Edge getEdge() {
        return edge;
    }
}
