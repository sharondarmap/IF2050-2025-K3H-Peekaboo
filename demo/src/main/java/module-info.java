module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    // tambahkan modul lain jika perlu

    // aplikasi Anda
    exports com.pekaboo;
    opens com.pekaboo to javafx.fxml;

    // FXML controller di fitur pembelian
    exports com.pekaboo.features.pembelian;
    opens com.pekaboo.features.pembelian to javafx.fxml;

    exports com.pekaboo.util;
    // tambahkan exports lain jika perlu
}
