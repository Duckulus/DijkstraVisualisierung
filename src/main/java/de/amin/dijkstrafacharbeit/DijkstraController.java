package de.amin.dijkstrafacharbeit;

import de.amin.dijkstrafacharbeit.data.Edge;
import de.amin.dijkstrafacharbeit.gui.EdgeLine;
import de.amin.dijkstrafacharbeit.gui.VertexPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.*;

public class DijkstraController {

    private Set<EdgeLine> edges = new HashSet<>();
    private Map<String, VertexPane> vertices = new HashMap<>();
    public static double sceneX, sceneY, layoutX, layoutY;
    public Pane pane;

    public void onEdgeAdd(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.setHeight(400);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        VBox dialogVbox = new VBox(30);
        dialog.setTitle("Add an Edge");
        Label error = new Label();

        Label label1 = new Label("Vertex 1:");
        TextField from = new TextField ();
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, from);
        hb.setSpacing(10);

        Label label2 = new Label("Vertex 2:");
        TextField to = new TextField ();
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label2, to);
        hb2.setSpacing(10);

        Label label3 = new Label("Weight: ");
        TextField weightField = new TextField ();
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(label3, weightField);
        hb3.setSpacing(10);



        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            if(!vertices.containsKey(from.getText())) { //Validate startpoint exists
                error.setText("Vertex " + from.getText() + " Not found");
                return;
            }

            if(!vertices.containsKey(to.getText())) { //Validate goal exists
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

            VertexPane start = vertices.get(from.getText());
            VertexPane end = vertices.get(to.getText());

            if(start==end) {
                error.setText("Vertex1 and Vertex2 cannot be the same");
                return;
            }

            Line line = new EdgeLine(start, end, pane, weight);
            pane.getChildren().add(line);
            dialog.hide();
        });

        dialogVbox.getChildren().addAll(hb, hb2, hb3, error, button);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void onVertexAdd(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(pane.getScene().getWindow());
        dialog.setResizable(false);
        VBox dialogVbox = new VBox(20);
        dialog.setTitle("Add a Vertex");

        Label error = new Label();

        Label inputLabel = new Label("Vertex Name: ");
        TextField field = new TextField();
        HBox hb = new HBox();
        hb.getChildren().addAll(inputLabel,field);
        hb.setSpacing(10);

        Button button = new Button();
        button.setText("add");
        button.setOnMouseClicked(event -> {
            for (String dot : vertices.keySet()) {
                if(dot.equalsIgnoreCase(field.getText())) {
                    error.setText("Vertex with this name already exists");
                    return;
                }
            }

            VertexPane vertex = new VertexPane(field.getText().replaceAll(" ", "").replaceAll("\n", "")); //Remove Spaces and Line Breaks
            vertices.put(field.getText(), vertex);
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
            Arrays.asList(vertices.keySet().toArray()).forEach(vertices::remove);
    }

    public void onPath(ActionEvent actionEvent) {
    }
}