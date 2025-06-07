package com.pekaboo.features.pembelian;

import java.io.IOException;

import com.pekaboo.entities.Product;
import com.pekaboo.util.checkout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class CheckoutController {
    @FXML private Label totalAmountLabel;
    //@FXML private Button removeProductButton;
    @FXML private Button checkoutButton;
    @FXML private Button addPrescriptionButton; 
    @FXML private Button plusPrescriptionButton;
    @FXML private Button minusPrescriptionButton;
    @FXML private Label prescriptionQuantityLabel; // jika ingin tampilkan jumlah prescription
    @FXML private Label productPriceLabel;
    @FXML private Label productNameLabel;
    @FXML private Label productBrandLabel; // Tambahkan field untuk brand produk

    @FXML private VBox cartItemsContainer;
    @FXML private Label emptyCartLabel;
    @FXML private VBox imageContainer;
    @FXML private VBox buttonContainer;
    @FXML private ImageView productImageView;

    @FXML private Rectangle colorBlack;
    @FXML private Rectangle colorBrown;

    private ObservableList<Product> cartItems;
    private int totalAmount = 0;
    private int prescriptionQuantity = 1;

    private checkout checkoutUtil = new checkout();

    // Tambahkan field untuk produk aktif
    private Product activeProduct;

    @FXML
    private void initialize() {
        cartItems = FXCollections.observableArrayList();
        if (totalAmountLabel != null) {
            totalAmountLabel.setVisible(false); // Sembunyikan dari display, logic tetap jalan
        }

        // if (removeProductButton != null) {
        //     removeProductButton.setDisable(true);
        // }

        addInitialProductToCart();

        if (addPrescriptionButton != null) {
            addPrescriptionButton.setOnAction(e -> handleAddPrescription());
        }

        // Ganti path gambar dengan yang pasti ada, dan tambahkan pengecekan null
        String imagePath = "/com/pekaboo/pembelian/assets/gambar.png";
        java.io.InputStream imgStream = getClass().getResourceAsStream(imagePath);
        if (imgStream != null) {
            Image img = new Image(imgStream);
            productImageView.setImage(img);
        } else {
            System.err.println("Image not found: " + imagePath);
            productImageView.setImage(null);
        }

        // Logic prescription +/-
        if (plusPrescriptionButton != null) {
            plusPrescriptionButton.setOnAction(e -> {
                prescriptionQuantity++;
                updatePrescriptionQuantityLabel();
                calculateTotalAmount();
                // updateTotalAmountLabel();
            });
        }
        if (minusPrescriptionButton != null) {
            minusPrescriptionButton.setOnAction(e -> {
                if (prescriptionQuantity > 1) {
                    prescriptionQuantity--;
                    updatePrescriptionQuantityLabel();
                    calculateTotalAmount();
                    // updateTotalAmountLabel();
                }
            });
        }
        updatePrescriptionQuantityLabel();
    }

    private void addInitialProductToCart() {
        Product sampleProduct = new Product();
        sampleProduct.setName("Sample Product");
        sampleProduct.setBrand("Brand Contoh"); // Tambahkan brand di sini
        sampleProduct.setPrice(100);
        sampleProduct.setImagePath("/com/pekaboo/pembelian/assets/gambar.png");

        addProductToCart(sampleProduct);
    }

    public void addProductToCart(Product product) {
        emptyCartLabel.setVisible(false);

        // Set produk aktif
        activeProduct = product;

        // Set nama produk pada label
        if (productBrandLabel != null) {
            productBrandLabel.setText(product.getBrand());
        }
        if (productNameLabel != null) {
            productNameLabel.setText(product.getName());
        }

        // Set image ke productImageView yang sudah ada di FXML
        Image img = new Image(getClass().getResource(product.getImagePath()).toExternalForm());
        productImageView.setImage(img);

        // Tampilkan harga produk pada label di buttonContainer
        if (productPriceLabel != null) {
            productPriceLabel.setText("Rp " + product.getPrice());
        }

        // Hapus logic minusBtn dan plusBtn di cart
        Label nameLabel      = new Label(product.getName());
        Label priceLabel     = new Label("Rp " + product.getPrice());
        Label quantityLabel  = new Label("1");
        HBox qtyBox = new HBox(10, quantityLabel); // hanya label quantity

        HBox productBox = new HBox(10, 
            nameLabel, 
            priceLabel, 
            qtyBox
        );

        cartItemsContainer.getChildren().add(productBox);
        cartItems.add(product);

        calculateTotalAmount();
        updateTotalAmountLabel(); // Tetap update logic, hanya label tidak tampil
    }

    /**
     * Remove a product from the checkout cart
     */
    @FXML
    private void handleRemoveProduct() {
        if (!cartItems.isEmpty()) {
            cartItems.remove(cartItems.size() - 1); 
            cartItemsContainer.getChildren().remove(cartItemsContainer.getChildren().size() - 1);
            calculateTotalAmount();
            updateTotalAmountLabel(); // Tetap update logic, hanya label tidak tampil
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
                // Index 1 = priceLabel
                Label priceL = (Label) row.getChildren().get(1); // priceLabel
                int price = Integer.parseInt(priceL.getText().replaceAll("[^0-9]", ""));
                totalAmount += price; // quantity produk selalu 1
            }
        }
        // Prescription: hanya tambahkan jika prescriptionQuantity > 1
        if (activeProduct != null && prescriptionQuantity > 1) {
            totalAmount += activeProduct.getPrice() * (prescriptionQuantity - 1);
        }
    }

    /**
     * Update the total amount label
     */
    private void updateTotalAmountLabel() {
        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("Total: Rp %d", totalAmount));
        }
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
        updateTotalAmountLabel(); // Tetap update logic, hanya label tidak tampil
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

    private void updatePrescriptionQuantityLabel() {
        if (prescriptionQuantityLabel != null) {
            prescriptionQuantityLabel.setText(String.valueOf(prescriptionQuantity));
        }
    }

    public void handleColorBlackClicked(MouseEvent event) {
        selectColor(colorBlack);
    }

    public void handleColorBrownClicked(MouseEvent event) {
        selectColor(colorBrown);
    }

    private void selectColor(Rectangle selected) {
        colorBlack.getStyleClass().remove("selected");
        colorBrown.getStyleClass().remove("selected");
        if (!selected.getStyleClass().contains("selected")) {
            selected.getStyleClass().add("selected");
        }
    }
}
