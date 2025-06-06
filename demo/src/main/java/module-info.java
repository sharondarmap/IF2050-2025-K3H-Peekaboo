module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.pekaboo to javafx.fxml;
    opens com.pekaboo.features.pembelian to javafx.fxml;
    opens com.pekaboo.entities to javafx.base;
    exports com.pekaboo;
}
