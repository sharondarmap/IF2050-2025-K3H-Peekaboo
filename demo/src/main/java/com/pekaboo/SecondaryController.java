package com.pekaboo;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void switchToJadwal() throws IOException {
        App.setRoot("jadwal/jadwal");
    }
}