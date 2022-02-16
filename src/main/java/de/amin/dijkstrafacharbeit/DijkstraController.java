package de.amin.dijkstrafacharbeit;

import de.amin.dijkstrafacharbeit.data.*;
import de.amin.dijkstrafacharbeit.gui.EdgeLine;
import de.amin.dijkstrafacharbeit.gui.VertexPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        dialog.setTitle("Add an Edge");
        Label error = new Label();

        Label label1 = new Label("Vertex 1:");
        TextField from = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, from);
        hb.setSpacing(10);

        Label label2 = new Label("Vertex 2:");
        TextField to = new TextField();
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label2, to);
        hb2.setSpacing(10);

        Label label3 = new Label("Weight: ");
        TextField weightField = new TextField();
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(label3, weightField);
        hb3.setSpacing(10);


        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            if (!vertices.containsKey(from.getText())) { //Validate startpoint exists
                error.setText("Vertex " + from.getText() + " Not found");
                return;
            }

            if (!vertices.containsKey(to.getText())) { //Validate goal exists
                error.setText("Vertex " + to.getText() + " Not found");
                return;
            }

            double weight;
            try {
                weight = Double.parseDouble(weightField.getText());
            } catch (NumberFormatException e) { //Validate weight can be converted to doulbe
                error.setText("Invalid value for Weight! Please enter a Number");
                return;
            }

            if(weight<0) {
                error.setText("Weight can not be negative!");
                return;
            }

            VertexPane start = vertices.get(from.getText());
            VertexPane end = vertices.get(to.getText());

            if (start == end) {
                error.setText("Vertex1 and Vertex2 cannot be the same");
                return;
            }

            for (EdgeLine edge : edges) {
                if(edge.getTo()==start && edge.getFrom()==end) {
                    error.setText("A connection between these Vertices already exists!");
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

        dialogVbox.getChildren().addAll(hb, hb2, hb3, error, button);
        Scene dialogScene = new Scene(dialogVbox, 300, 250);
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
        dialog.setTitle("Add a Vertex");

        Label error = new Label();

        Label inputLabel = new Label("Vertex Name: ");
        TextField field = new TextField();
        field.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        HBox hb = new HBox();
        hb.getChildren().addAll(inputLabel, field);
        hb.setSpacing(10);

        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            for (String dot : vertices.keySet()) {
                if (dot.equalsIgnoreCase(field.getText())) {
                    error.setText("Vertex with this name already exists");
                    return;
                }
            }

            if(field.getText().isEmpty()) {
                error.setText("Please Provide a valid name.");
                return;
            }

            String name = field.getText().replaceAll(" ", "").replaceAll("\n", "");
            VertexPane vertex = new VertexPane(name); //Remove Spaces and Line Breaks
            vertices.put(name, vertex);
            pane.getChildren().add(vertex);
            dialog.hide();
        });
        dialogVbox.getChildren().addAll(hb, button, error);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
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
        Label startLabel = new Label("Select Starting Point: ");
        ObservableList<Vertex> startOptions = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> startOptions.add(vertexPane.getVertex()));
        ComboBox<Vertex> startBox = new ComboBox<>(startOptions);
        startSelector.getChildren().addAll(startLabel, startBox);

        HBox goalSelector = new HBox();
        Label goalLabel = new Label("Select Goal Point: ");
        ObservableList<Vertex> goalOptions = FXCollections.observableArrayList();
        vertices.forEach((s, vertexPane) -> goalOptions.add(vertexPane.getVertex()));
        ComboBox<Vertex> goalBox = new ComboBox<>(goalOptions);
        goalSelector.getChildren().addAll(goalLabel, goalBox);



        Button button = new Button("Calculate Path");
        Label result = new Label();
        Label error = new Label();
        button.setOnAction(event -> {
            Vertex origin = startBox.getSelectionModel().getSelectedItem();
            Vertex goal = goalBox.getSelectionModel().getSelectedItem();
            if(origin == null || goal == null) {
                error.setText("Please select a start and a goal.");
                return;
            }
            Graph graph = new Graph();
            vertices.forEach((s, vertexPane) -> {
                graph.addVertex(vertexPane.getVertex());
            });

            edges.forEach(edgeLine -> {
                graph.addEdge(edgeLine.getEdge());
            });


            Dijkstra dijkstra = new Dijkstra();
            List<Vertex> path = dijkstra.shortestPath(graph,origin,goal);
            updateColors(graph, origin,goal,path);
            result.setText(dijkstra.toPathString(path));
        });

        dialogVbox.getChildren().addAll(startSelector, goalSelector, button, result, error);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void updateColors(Graph graph, Vertex start, Vertex end, List<Vertex> path) {
        //reset colors to default
        vertices.forEach((s, vertexPane) -> {
            vertexPane.setColor("cyan");
        });

        edges.forEach(edgeLine -> {
            edgeLine.setStroke(Color.BLACK);
        });


        vertices.forEach((s, vertexPane) -> {
            if(vertexPane.getVertex()==start) {
                vertexPane.setColor("#13e833");
            }
            if(vertexPane.getVertex()==end) {
                vertexPane.setColor("#e36f09");
            }
        });

        path.toFirst();
        while (path.hasAccess()) {
            Vertex temp = path.getContent();
            path.next();
            if(path.hasAccess()) {
                Edge edge = graph.getEdge(temp, path.getContent());
                edges.forEach(edgeLine -> {
                    if(edgeLine.getEdge() == edge) {
                        edgeLine.setStroke(Color.RED);
                    }
                });
            }
        }
    }

}