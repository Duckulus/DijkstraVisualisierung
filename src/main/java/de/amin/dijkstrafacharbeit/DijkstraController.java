package de.amin.dijkstrafacharbeit;

import de.amin.dijkstrafacharbeit.data.List;
import de.amin.dijkstrafacharbeit.data.*;
import de.amin.dijkstrafacharbeit.gui.EdgeLine;
import de.amin.dijkstrafacharbeit.gui.InputField;
import de.amin.dijkstrafacharbeit.gui.Popup;
import de.amin.dijkstrafacharbeit.gui.VertexPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

public class DijkstraController {

    private final Set<EdgeLine> edges = new HashSet<>();
    private final Map<String, VertexPane> vertices = new HashMap<>();
    public static double sceneX, sceneY, layoutX, layoutY;
    public Pane pane;

    public void onEdgeAdd(ActionEvent actionEvent) {
        Popup popup = new Popup("Kante hinzufügen", 350, 250);

        Label error = new Label();

        HBox vertex1Selector = new HBox();
        Label label1 = new Label("Wähle einen Knoten: ");
        ObservableList<Vertex> options1 = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> options1.add(vertexPane.getVertex()));
        ComboBox<Vertex> box1 = new ComboBox<>(options1);
        vertex1Selector.getChildren().addAll(label1, box1);

        HBox vertex2Selector = new HBox();
        Label label2 = new Label("Wähle einen Knoten: ");
        ObservableList<Vertex> options2 = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> options2.add(vertexPane.getVertex()));
        ComboBox<Vertex> box2 = new ComboBox<>(options2);
        vertex2Selector.getChildren().addAll(label2, box2);


        InputField weightField = new InputField("Kantengewicht: ");


        Button button = new Button("Erzeugen");
        button.setOnMouseClicked(event -> {
            double weight;

            try {
                weight = Double.parseDouble(weightField.getText());
            } catch (NumberFormatException e) { //Validate weight can be converted to doulbe
                error.setText("Ungülter Wert für Gewicht. Bitte gebe eine Zahl ein.");
                return;
            }

            if (weight < 0) {
                error.setText("Gewicht kann nicht negativ sein.");
                return;
            }

            VertexPane start = vertices.get(box1.getValue().getID());
            VertexPane end = vertices.get(box2.getValue().getID());

            if (start == end) {
                error.setText("Bitte wähle 2 verschiedene Knoten aus.");
                return;
            }

            for (EdgeLine edge : edges) {
                if (Arrays.stream(edge.getEdge().getVertices()).anyMatch(vertex -> vertex == start.getVertex()) &&
                        Arrays.stream(edge.getEdge().getVertices()).anyMatch(vertex -> vertex == end.getVertex())) {
                    error.setText("Verbindung besteht bereits");
                    return;
                }
            }

            EdgeLine line = new EdgeLine(start, end, pane, weight);
            pane.getChildren().addAll(line);
            pane.getChildren().removeAll(start, end);
            pane.getChildren().addAll(start, end); //remove and add vertices so they appear on top of the line
            popup.hide();
            edges.add(line);
        });

        popup.getBox().getChildren().addAll(vertex1Selector, vertex2Selector, weightField, error, button);

        popup.show();
    }

    public void onVertexAdd(ActionEvent actionEvent) {
        Popup popup = new Popup("Knoten hinzufügen", 350, 200);

        Label error = new Label();

        InputField inputField = new InputField("Knotenname: ");

        Button button = new Button();
        button.setText("Erzeugen");
        button.setOnMouseClicked(event -> {
            for (String dot : vertices.keySet()) {
                if (dot.equalsIgnoreCase(inputField.getText())) {
                    error.setText("Es existiert bereits ein Knoten mit diesem Namen: ");
                    return;
                }
            }

            if (inputField.getText().isEmpty()) {
                error.setText("Bitte gebe einen gültigen Namen ein");
                return;
            }

            String name = inputField.getText().replaceAll(" ", "").replaceAll("\n", "");
            VertexPane vertex = new VertexPane(name); //Remove Spaces and Line Breaks

            while (vertices.values().stream().anyMatch(vertexPane -> vertexPane.getLayoutX() == vertex.getLayoutX()
                    && vertexPane.getLayoutY() == vertex.getLayoutY())) {

                vertex.setLayoutX(vertex.getLayoutX() + 100); // Move to the right if position is already taken to prevent overlapping
                if (vertex.getLayoutX() > pane.getWidth()) {
                    vertex.setLayoutX(0);
                    vertex.setLayoutY(vertex.getLayoutY() + 100);
                }
            }

            vertices.put(name, vertex);
            pane.getChildren().add(vertex);
            popup.hide();
        });
        popup.getBox().getChildren().addAll(inputField, button, error);
        popup.show();
    }


    public void onReset(ActionEvent actionEvent) {
        pane.getChildren().removeAll(pane.getChildren());
        vertices.clear();
        edges.clear();
    }

    public void onPath(ActionEvent actionEvent) {
        Popup popup = new Popup("Pfad Berechnen", 300, 250);

        HBox startSelector = new HBox();
        Label startLabel = new Label("Wähle einen Startpunkt: ");
        ObservableList<Vertex> startOptions = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> startOptions.add(vertexPane.getVertex()));
        ComboBox<Vertex> startBox = new ComboBox<>(startOptions);
        startSelector.getChildren().addAll(startLabel, startBox);

        HBox goalSelector = new HBox();
        Label goalLabel = new Label("Wähle einen Zielpunkt: ");
        ObservableList<Vertex> goalOptions = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> goalOptions.add(vertexPane.getVertex()));
        ComboBox<Vertex> goalBox = new ComboBox<>(goalOptions);
        goalSelector.getChildren().addAll(goalLabel, goalBox);


        Button button = new Button("Pfad berechnen");
        Label result = new Label();
        Label error = new Label();
        Label cost = new Label("Gesamte Kosten: ");
        button.setOnAction(event -> {
            Vertex origin = startBox.getSelectionModel().getSelectedItem();
            Vertex goal = goalBox.getSelectionModel().getSelectedItem();
            if (origin == null || goal == null) {
                error.setText("Bitte wähle einen Startpunkt und einen Zielpunkt");
                return;
            }

            if (origin == goal) {
                error.setText("Bitte wähle 2 verschiedene Knoten aus");
                return;
            }


            Graph graph = new Graph();
            vertices.forEach((s, vertexPane) -> graph.addVertex(vertexPane.getVertex()));

            edges.forEach(edgeLine -> graph.addEdge(edgeLine.getEdge()));


            Dijkstra dijkstra = new Dijkstra();
            List<Vertex> path = dijkstra.shortestPath(graph, origin, goal);

            if (length(path) == 1) {
                error.setText("Es konnte kein Weg zwischen den ausgewählten Knoten gefunden werden!");
            }

            cost.setText("Gesamte Kosten: " + updateColors(graph, origin, goal, path));
            result.setText(dijkstra.toPathString(path));
        });

        popup.getBox().getChildren().addAll(startSelector, goalSelector, button, result, cost, error);
        popup.show();
    }

    private int updateColors(Graph graph, Vertex start, Vertex end, List<Vertex> path) {
        //reset colors to default
        vertices.forEach((s, vertexPane) -> vertexPane.setColor("cyan"));

        edges.forEach(edgeLine -> edgeLine.setStroke(Color.BLACK));


        vertices.forEach((s, vertexPane) -> {
            if (vertexPane.getVertex() == start) {
                vertexPane.setColor("#13e833");
            }
            if (vertexPane.getVertex() == end) {
                vertexPane.setColor("#e36f09");
            }
        });
        int cost = 0;
        path.toFirst();
        while (path.hasAccess()) {
            Vertex temp = path.getContent();
            path.next();
            if (path.hasAccess()) {
                Edge edge = graph.getEdge(temp, path.getContent());
                cost += edge.getWeight();
                edges.forEach(edgeLine -> {
                    if (edgeLine.getEdge() == edge) {
                        edgeLine.setStroke(Color.RED);
                    }
                });
            }
        }
        return cost;
    }

    private <T> int length(List<T> list) {
        int count = 0;
        list.toFirst();
        while (list.hasAccess()) {
            count++;
            list.next();
        }
        return count;
    }

}