package de.amin.dijkstrafacharbeit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the entrypoint to the Application
 *
 * @author Amin Haddou
 */
public class DijkstraApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DijkstraApplication.class.getResource("GraphView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(DijkstraApplication.class.getResource("style.css").toExternalForm());
        stage.setTitle("Dijkstra Visualization");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}