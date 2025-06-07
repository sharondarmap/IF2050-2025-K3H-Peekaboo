module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;

    // aplikasi Anda
    exports com.pekaboo;
    opens com.pekaboo to javafx.fxml;

    // FXML controller di fitur pembelian
    exports com.pekaboo.features.pembelian;
    opens com.pekaboo.features.pembelian to javafx.fxml;
}
