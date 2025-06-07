module com.pekaboo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql; 

    opens com.pekaboo to javafx.fxml;
    opens com.pekaboo.auth to javafx.fxml;
    opens com.pekaboo.features.home to javafx.fxml;
    opens com.pekaboo.features.navbar to javafx.fxml;
    opens com.pekaboo.features.footer to javafx.fxml;
    opens com.pekaboo.features.pembelian to javafx.fxml;
    opens com.pekaboo.features.reservasi to javafx.fxml;
    opens com.pekaboo.features.jadwal to javafx.fxml;
    opens com.pekaboo.entities to javafx.base;

    exports com.pekaboo.features.home; // untuk MainDriver.java
    exports com.pekaboo.features.pembelian; // catalogdriver
    
    exports com.pekaboo;
}
