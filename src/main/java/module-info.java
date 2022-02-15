module de.amin.dijkstrafacharbeit {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.amin.dijkstrafacharbeit to javafx.fxml;
    exports de.amin.dijkstrafacharbeit;
}