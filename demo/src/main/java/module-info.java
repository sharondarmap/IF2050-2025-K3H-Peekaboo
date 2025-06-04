module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Open FXML-based packages for controller injection
    opens com.pekaboo to javafx.fxml;
    opens com.pekaboo.util to javafx.fxml;

    // Controllers for each feature (used by FXMLLoader)
    opens com.pekaboo.features.jadwal to javafx.fxml;
    opens com.pekaboo.features.pembelian to javafx.fxml;
    opens com.pekaboo.features.reservasi to javafx.fxml;

    // Open entities package for TableView & property bindings
    opens com.pekaboo.entities to javafx.base;
}
