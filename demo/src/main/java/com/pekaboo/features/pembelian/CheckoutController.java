package com.pekaboo.features.pembelian;

import java.io.IOException;

import com.pekaboo.entities.Product;
import com.pekaboo.util.checkout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CheckoutController {
    @FXML private Label totalAmountLabel;
    @FXML private Button removeProductButton;
    @FXML private Button checkoutButton;
    @FXML private Button cancelButton;
    @FXML private Button addPrescriptionButton; // Button for adding prescription

    @FXML private VBox cartItemsContainer;
    @FXML private Label emptyCartLabel;
    @FXML private VBox productOptionsContainer; // Container for product options

    private ObservableList<Product> cartItems;
    private int totalAmount = 0;

    private checkout checkoutUtil = new checkout();

    @FXML
    private void initialize() {
        cartItems = FXCollections.observableArrayList();
        updateTotalAmountLabel();

        if (removeProductButton != null) {
            removeProductButton.setDisable(true);
        }

        addInitialProductToCart();

        if (addPrescriptionButton != null) {
            addPrescriptionButton.setOnAction(e -> handleAddPrescription());
        }
    }

    private void addInitialProductToCart() {
        Product sampleProduct = new Product();
        sampleProduct.setName("Sample Product");
        sampleProduct.setPrice(100);
        sampleProduct.setImagePath("/com/pekaboo/pembelian/assets/gambar.png");

        addProductToCart(sampleProduct);
    }

    private void displayProductOptions() {
        Product sampleProduct = new Product();
        sampleProduct.setName("Sample Product");
        sampleProduct.setPrice(100);

        HBox productBox = new HBox(10);
        Label nameLabel = new Label(sampleProduct.getName());
        Label priceLabel = new Label("Rp" + sampleProduct.getPrice());

        // Color options
        HBox colorOptions = new HBox(5);
        Label colorLabel = new Label("Color:");
        for (Color color : new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK}) {
            Circle colorCircle = new Circle(10, color);
            colorCircle.setStyle("-fx-stroke: black; -fx-stroke-width: 1;"); // Add border to circles
            colorCircle.setOnMouseClicked(e -> {
                // Handle color selection
                System.out.println("Selected color: " + color.toString());
            });
            colorOptions.getChildren().add(colorCircle);
        }

        // Size options
        HBox sizeOptions = new HBox(5);
        Label sizeLabel = new Label("Size:");
        for (String size : new String[]{"S", "M", "XL"}) {
            Button sizeButton = new Button(size);
            sizeButton.setStyle("-fx-font-size: 12px; -fx-padding: 5px;");
            sizeButton.setOnAction(e -> {
                // Handle size selection
                System.out.println("Selected size: " + size);
            });
            sizeOptions.getChildren().add(sizeButton);
        }

        productBox.getChildren().addAll(
            nameLabel,
            priceLabel,
            colorLabel,
            colorOptions,
            sizeLabel,
            sizeOptions
        );

        productOptionsContainer.getChildren().add(productBox);
    }

    /**
     * Add a product to the checkout cart
     */
    @FXML
    private void handleAddProduct() {
        Product sampleProduct = new Product();
        sampleProduct.setName("Sample Product");
        sampleProduct.setPrice(100);

        // Set quantity menggunakan util.checkout
        checkoutUtil.setQuantity(1);

        addProductToCart(sampleProduct);
    }

    public void addProductToCart(Product product) {
        emptyCartLabel.setVisible(false);

        HBox productBox = new HBox(10);
        Label nameLabel      = new Label(product.getName());
        Label priceLabel     = new Label("Rp " + product.getPrice());
        Label quantityLabel  = new Label("1");
        Button minusBtn      = new Button("–");
        Button plusBtn       = new Button("+");

        minusBtn.setDisable(true);
        minusBtn.setOnAction(e -> {
            int q = Integer.parseInt(quantityLabel.getText());
            if (q > 1) {
                q--;
                quantityLabel.setText(String.valueOf(q));
                minusBtn.setDisable(q == 1);
                calculateTotalAmount();
                updateTotalAmountLabel();
            }
        });
        plusBtn.setOnAction(e -> {
            int q = Integer.parseInt(quantityLabel.getText()) + 1;
            quantityLabel.setText(String.valueOf(q));
            minusBtn.setDisable(false);
            calculateTotalAmount();
            updateTotalAmountLabel();
        });

        productBox.getChildren().addAll(
            nameLabel,
            priceLabel,
            minusBtn,
            quantityLabel,
            plusBtn
        );
        cartItemsContainer.getChildren().add(productBox);
        cartItems.add(product);

        calculateTotalAmount();
        updateTotalAmountLabel();
    }

    /**
     * Remove a product from the checkout cart
     */
    @FXML
    private void handleRemoveProduct() {
        if (!cartItems.isEmpty()) {
            cartItems.remove(cartItems.size() - 1); // Menghapus produk terakhir sebagai contoh
            cartItemsContainer.getChildren().remove(cartItemsContainer.getChildren().size() - 1); // Hapus elemen dari VBox
            calculateTotalAmount();
            updateTotalAmountLabel();

            // Jika keranjang kosong, tampilkan label "No items in the cart"
            if (cartItems.isEmpty()) {
                emptyCartLabel.setVisible(true);
            }
        }
    }

    /**
     * Calculate the total amount for all items in the cart
     */
    private void calculateTotalAmount() {
        totalAmount = 0;
        for (Node node : cartItemsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                Label priceL = (Label) row.getChildren().get(1);
                Label qtyL   = (Label) row.getChildren().get(3);
                int price = Integer.parseInt(priceL.getText().replaceAll("[^0-9]", ""));
                int qty   = Integer.parseInt(qtyL.getText());
                totalAmount += price * qty;
            }
        }
    }

    /**
     * Update the total amount label
     */
    private void updateTotalAmountLabel() {
        totalAmountLabel.setText(String.format("Total: Rp %d", totalAmount));
    }

    /**
     * Process the checkout
     */
    @FXML
    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Error", "Cart is empty", "Please add products before checking out.");
            return;
        }

        // In a real application, this would handle payment processing,
        // inventory updates, order creation, etc.
        boolean success = true;

        if (success) {
            showAlert("Success", "Checkout Complete", "Your order has been processed successfully!");
            clearCart();
        }
    }

    /**
     * Cancel the checkout process
     */
    @FXML
    private void handleCancel() {
        try {
            switchToMainMenu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear the shopping cart
     */
    private void clearCart() {
        cartItems.clear();
        cartItemsContainer.getChildren().clear();
        totalAmount = 0;
        updateTotalAmountLabel();
        emptyCartLabel.setVisible(true);
    }

    /**
     * Handle adding prescription
     */
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
        //import com.pekaboo.App;
    }
}
