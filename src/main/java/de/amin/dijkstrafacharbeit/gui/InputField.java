package de.amin.dijkstrafacharbeit.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

/**
 * Diese Klasse fasst ein {@link TextField} und ein {@link Label}
 * zusammen
 *
 * @author Amin Haddou
 */
public class InputField extends HBox {

    private TextField textField;

    public InputField(String label) {
        super();
        Label inputLabel = new Label(label);
        this.textField  = new TextField();
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        this.getChildren().addAll(inputLabel, textField);
        this.setSpacing(10);
    }

    public String getText() {
        return textField.getText();
    }

}
