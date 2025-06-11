package com.pekaboo;
import com.pekaboo.features.home.MainController;
// import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void switchToSecondary() {
        mainController.loadPage("/com/pekaboo/secondary/Secondary.fxml", null);
    }

    @FXML
    private void switchToReservasi() {
        mainController.loadPage("/com/pekaboo/features/reservasi/Reservasi.fxml", null);
    }
}
