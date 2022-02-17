package de.amin.dijkstrafacharbeit.gui;

import de.amin.dijkstrafacharbeit.DijkstraApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Popup extends Stage {

    private VBox box;

    public Popup(String title, int width, int height) {
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);
        box = new VBox(20);
        box.setPadding(new Insets(16));
        box.setAlignment(Pos.CENTER);
        setTitle(title);
        Scene dialogScene = new Scene(box, width, height);
        dialogScene.getStylesheets().add(DijkstraApplication.class.getResource("style.css").toExternalForm());
        setScene(dialogScene);
    }

    public VBox getBox() {
        return box;
    }
}
