package com.pekaboo.features.pembelian;

import java.io.IOException;

import com.pekaboo.entities.Product;
import com.pekaboo.util.checkout;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class CheckoutController {
    @FXML private Label totalAmountLabel;
    @FXML private Button checkoutButton;
    @FXML private Button addPrescriptionButton; 
    @FXML private Button plusPrescriptionButton;
    @FXML private Button minusPrescriptionButton;
    @FXML private Label prescriptionQuantityLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productNameLabel;
    @FXML private Label productBrandLabel;
    @FXML private Label productSizeLabel;
    @FXML private VBox imageContainer;
    @FXML private VBox buttonContainer;
    @FXML private ImageView productImageView;
    @FXML private Rectangle productColorRectangle;

    private int totalAmount = 0;
    private int prescriptionQuantity = 1;
    private checkout checkoutUtil = new checkout();
    private Product activeProduct;

    @FXML
    private void initialize() {
        if (addPrescriptionButton != null) {
            addPrescriptionButton.setOnAction(e -> handleAddPrescription());
        }

        if (plusPrescriptionButton != null) {
            plusPrescriptionButton.setOnAction(e -> {
                prescriptionQuantity++;
                updatePrescriptionQuantityLabel();
                calculateTotalAmount();
            });
        }
        if (minusPrescriptionButton != null) {
            minusPrescriptionButton.setOnAction(e -> {
                if (prescriptionQuantity > 1) {
                    prescriptionQuantity--;
                    updatePrescriptionQuantityLabel();
                    calculateTotalAmount();
                }
            });
        }
        updatePrescriptionQuantityLabel();
        if (activeProduct != null) {
            setActiveProduct(activeProduct);
        }
    }

    private void calculateTotalAmount() {
        if (activeProduct != null) {
            totalAmount = activeProduct.getPrice() * prescriptionQuantity;
        } else {
            totalAmount = 0;
        }
    }

    private void updateTotalAmountLabel() {
        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("Total: Rp %d", totalAmount));
        }
    }

    @FXML
    private void handleCheckout() {
        boolean success = true;
        if (success) {
            showAlert("Success", "Checkout Complete", "Your order has been processed successfully!");
            clearCart();
        }
    }

    @FXML
    private void handleCancel() {
        try {
            switchToMainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearCart() {
        totalAmount = 0;
        updateTotalAmountLabel();
    }

    private void handleAddPrescription() {
        showAlert("Prescription", "Add Prescription", "Prescription functionality is not yet implemented.");
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void switchToMainMenu() throws IOException {
    }

    private void updatePrescriptionQuantityLabel() {
        if (prescriptionQuantityLabel != null) {
            prescriptionQuantityLabel.setText(String.valueOf(prescriptionQuantity));
        }
    }

    public void setActiveProduct(Product product) {
        this.activeProduct = product;
        prescriptionQuantity = 1;
        updatePrescriptionQuantityLabel();

        if (productNameLabel != null) productNameLabel.setText(product.getName());
        if (productBrandLabel != null) productBrandLabel.setText(product.getBrand());
        if (productPriceLabel != null) productPriceLabel.setText(String.format("Rp %d", product.getPrice()));
        if (productSizeLabel != null) productSizeLabel.setText(product.getSize());
        if (productColorRectangle != null) {
            String fill = mapColor(product.getColor());
            productColorRectangle.setStyle("-fx-fill: " + fill + ";");
        }
        if (productImageView != null && product.getImagePath() != null) {
            java.io.InputStream imgStream = getClass().getResourceAsStream(product.getImagePath());
            if (imgStream != null) {
                Image img = new Image(imgStream);
                productImageView.setImage(img);
            } else {
                productImageView.setImage(null);
            }
        }
        calculateTotalAmount();
        updateTotalAmountLabel();
    }

    private String mapColor(String color) {
        if (color == null) return "transparent";
        switch (color.toLowerCase()) {
            case "hitam":  return "#000000";
            case "merah":  return "#FF5454";
            case "biru":   return "#0118AB";
            case "hijau":  return "#51A532";
            case "kuning": return "#EFEA72";
            case "orange": return "#FA5";
            case "ungu":   return "#622C92";
            case "coklat": return "#764B00";
            default:
                return color.startsWith("#") ? color : color;
        }
    }
}
