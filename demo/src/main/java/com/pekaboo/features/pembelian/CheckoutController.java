package com.pekaboo.features.pembelian;

import java.io.IOException;

import com.pekaboo.entities.Product;
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
    private Product activeProduct;

    @FXML
    private void initialize() {
        if (addPrescriptionButton != null) {
            addPrescriptionButton.setOnAction(e -> showPrescriptionPopup());
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

    private void showPrescriptionPopup() {
        // Create overlay
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setAlignment(javafx.geometry.Pos.CENTER);
        javafx.scene.Scene scene = buttonContainer.getScene();
        if (scene == null) return;
        overlay.setPrefWidth(scene.getWidth());
        overlay.setPrefHeight(scene.getHeight());

        // Create popup content (bigger, but not too big)
        VBox popup = new VBox(24);
        popup.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 48px 40px;" +
            "-fx-background-radius: 28;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 24, 0, 0, 12);"
        );
        popup.setMaxWidth(600); // bigger width
        popup.setMinWidth(480);

        // Title
        Label title = new Label("Add Prescription");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #222;");

        // Section builder (adjust input width)
        VBox sphereSection = createPrescriptionSection("Sphere (±)", "#7B61FF", 160);
        VBox cylinderSection = createPrescriptionSection("Cylinder (Cyl)", "#7B61FF", 160);
        VBox axisSection = createPrescriptionSection("Axis", "#7B61FF", 160);
        VBox pdSection = createSingleFieldSection("PD (Pupillary Distance)", 340);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 13px; -fx-padding: 4 0 0 0;");
        errorLabel.setVisible(false);

        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(32); // <-- tambahkan spacing, misal 32px
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setFillHeight(true);

        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button("Save");

        cancelBtn.setPrefWidth(240);
        saveBtn.setPrefWidth(240);
        cancelBtn.setPrefHeight(44);
        saveBtn.setPrefHeight(44);

        cancelBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #7B61FF; " +
            "-fx-text-fill: #7B61FF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 0 0 0 0; " +
            "-fx-font-size: 15px;"
        );
        saveBtn.setStyle(
            "-fx-background-color: #7B61FF; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-padding: 0 0 0 0; " +
            "-fx-font-size: 15px;"
        );

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        // Jarak antara Cancel dan Save adalah seluruh ruang kosong di antara kedua button,
        // diisi oleh spacer (Region) di tengah. Jika popup diperlebar, jarak di antara kedua button juga melebar.

        // Add errorLabel above buttonBox if needed
        javafx.scene.layout.VBox errorAndButtons = new javafx.scene.layout.VBox(8);
        errorAndButtons.setFillWidth(true);
        errorAndButtons.getChildren().addAll(errorLabel, buttonBox);

        popup.getChildren().addAll(
            title,
            sphereSection,
            cylinderSection,
            axisSection,
            pdSection,
            errorAndButtons
        );

        // Add errorLabel below PD section
        ((VBox) pdSection).getChildren().add(errorLabel);

        overlay.getChildren().add(popup);

        // Add overlay to root pane
        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof javafx.scene.layout.Pane) {
            javafx.scene.layout.Pane pane = (javafx.scene.layout.Pane) root;
            pane.getChildren().add(overlay);
            overlay.toFront();
            cancelBtn.setOnAction(ev -> pane.getChildren().remove(overlay));
            saveBtn.setOnAction(ev -> pane.getChildren().remove(overlay));
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) pane.getChildren().remove(overlay);
            });
        } else {
            // If root is VBox/BorderPane, wrap with StackPane for overlay support
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().add(root);
            stackPane.getChildren().add(overlay);
            scene.setRoot(stackPane);
            overlay.toFront();
            cancelBtn.setOnAction(ev -> stackPane.getChildren().remove(overlay));
            saveBtn.setOnAction(ev -> stackPane.getChildren().remove(overlay));
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) stackPane.getChildren().remove(overlay);
            });
        }

        // --- VALIDATION LOGIC ON SAVE ---
        saveBtn.setOnAction(ev -> {
            java.util.List<javafx.scene.control.TextField> fields = new java.util.ArrayList<>();
            collectTextFields(sphereSection, fields);
            collectTextFields(cylinderSection, fields);
            collectTextFields(axisSection, fields);
            collectTextFields(pdSection, fields);

            boolean allNumeric = true;
            for (javafx.scene.control.TextField tf : fields) {
                String text = tf.getText();
                if (text == null || text.trim().isEmpty() || !text.matches("-?\\d+(\\.\\d+)?")) {
                    allNumeric = false;
                    break;
                }
            }
            if (!allNumeric) {
                errorLabel.setText("Semua input prescription harus berupa angka.");
                errorLabel.setVisible(true);
                return;
            }
            errorLabel.setVisible(false);

            javafx.scene.Parent currentRoot = buttonContainer.getScene().getRoot();
            if (currentRoot instanceof javafx.scene.layout.Pane) {
                ((javafx.scene.layout.Pane) currentRoot).getChildren().remove(overlay);
            } else if (currentRoot instanceof javafx.scene.layout.StackPane) {
                ((javafx.scene.layout.StackPane) currentRoot).getChildren().remove(overlay);
            }
        });
    }

    private VBox createPrescriptionSection(String label, String color, double inputWidth) {
        VBox section = new VBox(6);
        Label sectionLabel = new Label(label);
        sectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(18);
        row.setAlignment(javafx.geometry.Pos.CENTER);
        row.setFillHeight(true);

        if (label.toLowerCase().contains("sphere")) {
            double boxWidth = (inputWidth * 2 + 40) / 4.0; // 40 for spacing, adjust as needed

            VBox rightPlusBox = new VBox(2);
            Label rightPlusLabel = new Label("Right +");
            rightPlusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField rightPlusField = new javafx.scene.control.TextField();
            rightPlusField.setPrefWidth(boxWidth);
            rightPlusField.setPrefHeight(44);
            rightPlusField.setStyle("-fx-font-size: 15px;");
            rightPlusBox.setFillWidth(true);
            rightPlusBox.getChildren().addAll(rightPlusLabel, rightPlusField);

            VBox leftPlusBox = new VBox(2);
            Label leftPlusLabel = new Label("Left +");
            leftPlusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField leftPlusField = new javafx.scene.control.TextField();
            leftPlusField.setPrefWidth(boxWidth);
            leftPlusField.setPrefHeight(44);
            leftPlusField.setStyle("-fx-font-size: 15px;");
            leftPlusBox.setFillWidth(true);
            leftPlusBox.getChildren().addAll(leftPlusLabel, leftPlusField);

            VBox rightMinusBox = new VBox(2);
            Label rightMinusLabel = new Label("Right -");
            rightMinusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField rightMinusField = new javafx.scene.control.TextField();
            rightMinusField.setPrefWidth(boxWidth);
            rightMinusField.setPrefHeight(44);
            rightMinusField.setStyle("-fx-font-size: 15px;");
            rightMinusBox.setFillWidth(true);
            rightMinusBox.getChildren().addAll(rightMinusLabel, rightMinusField);

            VBox leftMinusBox = new VBox(2);
            Label leftMinusLabel = new Label("Left -");
            leftMinusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField leftMinusField = new javafx.scene.control.TextField();
            leftMinusField.setPrefWidth(boxWidth);
            leftMinusField.setPrefHeight(44);
            leftMinusField.setStyle("-fx-font-size: 15px;");
            leftMinusBox.setFillWidth(true);
            leftMinusBox.getChildren().addAll(leftMinusLabel, leftMinusField);

            // Make all boxes expand equally
            javafx.scene.layout.HBox.setHgrow(rightPlusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(leftPlusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(rightMinusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(leftMinusBox, javafx.scene.layout.Priority.ALWAYS);

            row.getChildren().addAll(rightPlusBox, leftPlusBox, rightMinusBox, leftMinusBox);
        } else {
            // ...existing code for non-sphere (2 input)...
            VBox rightBox = new VBox(2);
            Label rightLabel = new Label("Right");
            rightLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField rightField = new javafx.scene.control.TextField();
            rightField.setPrefWidth(inputWidth);
            rightField.setPrefHeight(44);
            rightField.setStyle("-fx-font-size: 15px;");
            rightBox.setFillWidth(true);
            rightBox.getChildren().addAll(rightLabel, rightField);

            VBox leftBox = new VBox(2);
            Label leftLabel = new Label("Left");
            leftLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            javafx.scene.control.TextField leftField = new javafx.scene.control.TextField();
            leftField.setPrefWidth(inputWidth);
            leftField.setPrefHeight(44);
            leftField.setStyle("-fx-font-size: 15px;");
            leftBox.setFillWidth(true);
            leftBox.getChildren().addAll(leftLabel, leftField);

            javafx.scene.layout.HBox.setHgrow(rightBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(leftBox, javafx.scene.layout.Priority.ALWAYS);

            row.getChildren().addAll(rightBox, leftBox);
        }
        section.getChildren().addAll(sectionLabel, row);
        return section;
    }

    private VBox createSingleFieldSection(String label, double inputWidth) {
        VBox section = new VBox(6);
        Label sectionLabel = new Label(label);
        sectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7B61FF;");
        javafx.scene.control.TextField field = new javafx.scene.control.TextField();
        field.setPrefWidth(inputWidth);
        field.setPrefHeight(44); 
        field.setStyle("-fx-font-size: 15px;");
        section.getChildren().addAll(sectionLabel, field);
        return section;
    }

    // Helper to collect all TextFields recursively from a parent node
    private void collectTextFields(javafx.scene.Parent parent, java.util.List<javafx.scene.control.TextField> fields) {
        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof javafx.scene.control.TextField) {
                fields.add((javafx.scene.control.TextField) node);
            } else if (node instanceof javafx.scene.Parent) {
                collectTextFields((javafx.scene.Parent) node, fields);
            }
        }
    }
}
