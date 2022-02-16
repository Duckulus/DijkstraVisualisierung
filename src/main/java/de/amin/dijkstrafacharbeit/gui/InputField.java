package de.amin.dijkstrafacharbeit.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

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

    public void setText(String text) {
        textField.setText(text);
    }

    public String getText() {
        return textField.getText();
    }

}
