module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.pekaboo to javafx.fxml;
    exports com.pekaboo;
}
