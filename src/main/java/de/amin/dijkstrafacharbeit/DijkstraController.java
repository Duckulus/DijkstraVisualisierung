package de.amin.dijkstrafacharbeit;

import de.amin.dijkstrafacharbeit.data.List;
import de.amin.dijkstrafacharbeit.data.*;
import de.amin.dijkstrafacharbeit.gui.EdgeLine;
import de.amin.dijkstrafacharbeit.gui.InputField;
import de.amin.dijkstrafacharbeit.gui.VertexPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

public class DijkstraController {

    private final Set<EdgeLine> edges = new HashSet<>();
    private final Map<String, VertexPane> vertices = new HashMap<>();
    public static double sceneX, sceneY, layoutX, layoutY;
    public Pane pane;

    public void onEdgeAdd(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        VBox dialogVbox = new VBox(30);
        dialogVbox.setPadding(new Insets(16));
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setTitle("Kante hinzufügen");
        Label error = new Label();

        InputField vertex1 = new InputField("Vertex 1:");

        InputField vertex2 = new InputField("Vertex 2:");

        InputField weightField = new InputField("Kantengewicht: ");


        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            if (!vertices.containsKey(vertex1.getText())) { //Validate startpoint exists
                error.setText("Knoten " + vertex2.getText() + " nicht gefunden");
                return;
            }

            if (!vertices.containsKey(vertex2.getText())) { //Validate goal exists
                error.setText("Knoten " + vertex2.getText() + " nicht gefunden");
                return;
            }

            double weight;
            try {
                weight = Double.parseDouble(weightField.getText());
            } catch (NumberFormatException e) { //Validate weight can be converted to doulbe
                error.setText("Ungülter Wert für Gewicht. Bitte gebe eine Zahl ein.");
                return;
            }

            if(weight<0) {
                error.setText("Gewicht kann nicht negativ sein.");
                return;
            }

            VertexPane start = vertices.get(vertex1.getText());
            VertexPane end = vertices.get(vertex2.getText());

            if (start == end) {
                error.setText("Vertex1 und Vertex2 können nicht gleich sein");
                return;
            }

            for (EdgeLine edge : edges) {
                if(Arrays.stream(edge.getEdge().getVertices()).anyMatch(vertex -> vertex==start.getVertex()) &&
                        Arrays.stream(edge.getEdge().getVertices()).anyMatch(vertex -> vertex==end.getVertex())) {
                    error.setText("Verbindung besteht bereits");
                    return;
                }
            }

            EdgeLine line = new EdgeLine(start, end, pane, weight);
            pane.getChildren().addAll(line);
            pane.getChildren().removeAll(start,end);
            pane.getChildren().addAll(start, end); //remove and add vertices so they appear on top of the line
            dialog.hide();
            edges.add(line);
        });

        dialogVbox.getChildren().addAll(vertex1, vertex2, weightField, error, button);
        Scene dialogScene = new Scene(dialogVbox, 350, 250);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void onVertexAdd(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        dialog.setResizable(false);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(16));
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setTitle("Knoten hinzufügen");

        Label error = new Label();

        InputField inputField = new InputField("Knotenname: ");

        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            for (String dot : vertices.keySet()) {
                if (dot.equalsIgnoreCase(inputField.getText())) {
                    error.setText("Es existiert bereits ein Knoten mit diesem Namen: ");
                    return;
                }
            }

            if(inputField.getText().isEmpty()) {
                error.setText("Bitte gebe einen gültigen Namen ein");
                return;
            }

            String name = inputField.getText().replaceAll(" ", "").replaceAll("\n", "");
            VertexPane vertex = new VertexPane(name); //Remove Spaces and Line Breaks
            vertices.put(name, vertex);
            pane.getChildren().add(vertex);
            dialog.hide();
        });
        dialogVbox.getChildren().addAll(inputField, button, error);

        Scene dialogScene = new Scene(dialogVbox, 350, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    public void onReset(ActionEvent actionEvent) {
        pane.getChildren().removeAll(pane.getChildren());
        vertices.clear();
        edges.clear();
    }

    public void onPath(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        dialog.setResizable(false);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(16));
        dialogVbox.setAlignment(Pos.CENTER);
        dialog.setTitle("Calculate Path to Goal");

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
            if(origin == null || goal == null) {
                error.setText("Bitte wähle einen Startpunkt und einen Zielpunkt");
                return;
            }
            Graph graph = new Graph();
            vertices.forEach((s, vertexPane) -> graph.addVertex(vertexPane.getVertex()));

            edges.forEach(edgeLine -> graph.addEdge(edgeLine.getEdge()));


            Dijkstra dijkstra = new Dijkstra();
            List<Vertex> path = dijkstra.shortestPath(graph,origin,goal);
            cost.setText("Gesamte Kosten: " + updateColors(graph, origin,goal,path));
            result.setText(dijkstra.toPathString(path));
        });

        dialogVbox.getChildren().addAll(startSelector, goalSelector, button, result, cost, error);

        Scene dialogScene = new Scene(dialogVbox, 350, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private int updateColors(Graph graph, Vertex start, Vertex end, List<Vertex> path) {
        //reset colors to default
        vertices.forEach((s, vertexPane) -> vertexPane.setColor("cyan"));

        edges.forEach(edgeLine -> edgeLine.setStroke(Color.BLACK));


        vertices.forEach((s, vertexPane) -> {
            if(vertexPane.getVertex()==start) {
                vertexPane.setColor("#13e833");
            }
            if(vertexPane.getVertex()==end) {
                vertexPane.setColor("#e36f09");
            }
        });
        int cost = 0;
        path.toFirst();
        while (path.hasAccess()) {
            Vertex temp = path.getContent();
            path.next();
            if(path.hasAccess()) {
                Edge edge = graph.getEdge(temp, path.getContent());
                cost+=edge.getWeight();
                edges.forEach(edgeLine -> {
                    if(edgeLine.getEdge() == edge) {
                        edgeLine.setStroke(Color.RED);
                    }
                });
            }
        }
        return cost;
    }

}