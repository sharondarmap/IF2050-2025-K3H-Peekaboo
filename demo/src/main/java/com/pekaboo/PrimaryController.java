package com.pekaboo;

import java.io.IOException;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToCheckout() throws IOException {
        System.out.println("Switching to Checkout...");
        App.setRoot("pembelian/checkout");
    }
}
