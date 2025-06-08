package com.pekaboo.features.pembelian;

import java.io.IOException;

import com.pekaboo.entities.Pesanan;
import com.pekaboo.entities.Product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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
    @FXML private HBox mainContainer; 

    private int totalAmount = 0;
    private int prescriptionQuantity = 1;
    private Product activeProduct;
    private boolean prescriptionFilled = false; 
    private Label checkoutErrorLabel = new Label();
    private com.pekaboo.entities.User user;
    private Pesanan pesanan;

    @FXML
    private void initialize() {
        // // --- BACK BUTTON ---
        // ImageView backIcon = new ImageView(
        //     new Image(getClass().getResourceAsStream("/com/pekaboo/pembelian/assets/back.png"))
        // );
        // backIcon.setFitWidth(18);
        // backIcon.setFitHeight(18);

        // Label backLabel = new Label("Back");
        // backLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");

        // javafx.scene.layout.HBox backBox = new javafx.scene.layout.HBox(8, backIcon, backLabel);
        // backBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        // backBox.setStyle("-fx-cursor: hand; -fx-padding: 18 0 18 0;");

        // backBox.setOnMouseClicked(e -> {
        //     try {
        //         switchToMainMenu();
        //     } catch (IOException ex) {
        //         ex.printStackTrace();
        //     }
        // });

        // if (mainContainer != null) {
        //     mainContainer.getChildren().add(0, backBox);
        // }

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

        javafx.scene.layout.HBox infoRow = new javafx.scene.layout.HBox(12);
        infoRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        ImageView checklistIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/com/pekaboo/pembelian/assets/checklist.png"))
        );
        checklistIcon.setFitWidth(22);
        checklistIcon.setFitHeight(22);

        Label availableStockLabel = new Label("Available in Stock");
        availableStockLabel.setStyle("-fx-text-fill: #7B61FF; -fx-font-size: 15px; -fx-font-weight: bold;");

        javafx.scene.layout.HBox availableStockBox = new javafx.scene.layout.HBox(6, checklistIcon, availableStockLabel);
        availableStockBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        checkoutErrorLabel.setText("Silakan isi prescription terlebih dahulu sebelum checkout.");
        checkoutErrorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 13px; -fx-padding: 0; -fx-max-width: 1000px; -fx-label-padding: 0; -fx-wrap-text: false;");
        checkoutErrorLabel.setVisible(false);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        infoRow.getChildren().addAll(availableStockBox, spacer, checkoutErrorLabel);

        if (buttonContainer != null) {
            buttonContainer.getChildren().remove(checkoutErrorLabel); 
            buttonContainer.getChildren().removeIf(node -> node instanceof javafx.scene.layout.HBox && ((javafx.scene.layout.HBox) node).getChildren().contains(availableStockBox));
            int idx = buttonContainer.getChildren().indexOf(checkoutButton);
            if (idx >= 0) {
                buttonContainer.getChildren().add(idx + 1, infoRow);
            } else {
                buttonContainer.getChildren().add(infoRow);
            }
        }
    }

    private void calculateTotalAmount() {
        if (activeProduct != null) {
            totalAmount = activeProduct.getPrice() * prescriptionQuantity;
        } else {
            totalAmount = 0;
        }
        if (pesanan != null) {
            pesanan.setTotalPesanan(totalAmount);
        }
    }

    private void updateTotalAmountLabel() {
        if (totalAmountLabel != null) {
            totalAmountLabel.setText(String.format("Total: Rp %d", totalAmount));
        }
    }

    @FXML
    private void handleCheckout() {
        if (!prescriptionFilled) {
            checkoutErrorLabel.setVisible(true);
            return;
        }
        checkoutErrorLabel.setVisible(false);
        showOrderConfirmationPopup();
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

    @FXML
    private void switchToMainMenu() throws IOException {
        //ini buat ke page menu back sebelumnya ntar diisi
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
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setAlignment(javafx.geometry.Pos.CENTER);
        javafx.scene.Scene scene = buttonContainer.getScene();
        if (scene == null) return;
        overlay.setPrefWidth(scene.getWidth());
        overlay.setPrefHeight(scene.getHeight());

        VBox popup = new VBox(24);
        popup.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 48px 40px;" +
            "-fx-background-radius: 28;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 24, 0, 0, 12);"
        );
        popup.setMaxWidth(600); 
        popup.setMinWidth(480);

        Label title = new Label("Add Prescription");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #222;");

        VBox sphereSection = createPrescriptionSection("Sphere (±)", "#7B61FF", 160);
        VBox cylinderSection = createPrescriptionSection("Cylinder (Cyl)", "#7B61FF", 160);
        VBox axisSection = createPrescriptionSection("Axis", "#7B61FF", 160);
        VBox pdSection = createSingleFieldSection("PD (Pupillary Distance)", 340);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 13px; -fx-padding: 4 0 0 0;");
        errorLabel.setVisible(false);

        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(32); 
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

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
            "-fx-font-size: 15px;"
        );
        saveBtn.setStyle(
            "-fx-background-color: #7B61FF; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8; " +
            "-fx-font-size: 15px;"
        );

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        VBox errorAndButtons = new VBox(8, errorLabel, buttonBox);

        popup.getChildren().addAll(
            title,
            sphereSection,
            cylinderSection,
            axisSection,
            pdSection,
            errorAndButtons
        );

        overlay.getChildren().add(popup);

        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof javafx.scene.layout.Pane) {
            ((javafx.scene.layout.Pane) root).getChildren().add(overlay);
            overlay.toFront();
            cancelBtn.setOnAction(ev -> ((javafx.scene.layout.Pane) root).getChildren().remove(overlay));
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
                prescriptionFilled = true;

                if (root instanceof javafx.scene.layout.Pane) {
                    ((javafx.scene.layout.Pane) root).getChildren().remove(overlay);
                } else if (root instanceof javafx.scene.layout.StackPane) {
                    ((javafx.scene.layout.StackPane) root).getChildren().remove(overlay);
                }
            });
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) ((javafx.scene.layout.Pane) root).getChildren().remove(overlay);
            });
        } else {
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
    }

    private VBox createPrescriptionSection(String label, String color, double inputWidth) {
        VBox section = new VBox(6);
        Label sectionLabel = new Label(label);
        sectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        javafx.scene.layout.HBox row = new javafx.scene.layout.HBox(18);
        row.setAlignment(javafx.geometry.Pos.CENTER);
        row.setFillHeight(true);

        if (label.toLowerCase().contains("sphere")) {
            double boxWidth = (inputWidth * 2 + 40) / 4.0;

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

            javafx.scene.layout.HBox.setHgrow(rightPlusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(leftPlusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(rightMinusBox, javafx.scene.layout.Priority.ALWAYS);
            javafx.scene.layout.HBox.setHgrow(leftMinusBox, javafx.scene.layout.Priority.ALWAYS);

            row.getChildren().addAll(rightPlusBox, leftPlusBox, rightMinusBox, leftMinusBox);
        } else {
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

    private void collectTextFields(javafx.scene.Parent parent, java.util.List<javafx.scene.control.TextField> fields) {
        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof javafx.scene.control.TextField) {
                fields.add((javafx.scene.control.TextField) node);
            } else if (node instanceof javafx.scene.Parent) {
                collectTextFields((javafx.scene.Parent) node, fields);
            }
        }
    }

    private void showOrderConfirmationPopup() {
        javafx.scene.Scene scene = buttonContainer.getScene();
        if (scene == null) return;

        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setAlignment(javafx.geometry.Pos.CENTER);
        overlay.setPrefWidth(scene.getWidth());
        overlay.setPrefHeight(scene.getHeight());

        VBox popup = new VBox(22);
        popup.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 56px 48px;" +
            "-fx-background-radius: 32;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 28, 0, 0, 14);"
        );
        popup.setMaxWidth(700); 
        popup.setMinWidth(540);
        popup.setAlignment(javafx.geometry.Pos.TOP_LEFT);

        Label addressTitle = new Label("Delivery Address");
        addressTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222;");

        VBox addressBox = new VBox(6);
        addressBox.setStyle("-fx-background-color: #F8F6FF; -fx-border-color: #E0D7FF; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12 18 12 18;");
        javafx.scene.layout.HBox namePhoneRow = new javafx.scene.layout.HBox();
        Label nameLabel = new Label(user != null && user.getUsername() != null ? user.getUsername() : "");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7B61FF;");
        Label phoneLabel = new Label(user != null && user.getNoTelepon() != null ? user.getNoTelepon() : "");
        phoneLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #7B61FF;");

        phoneLabel.setMaxWidth(Double.MAX_VALUE);
        phoneLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        namePhoneRow.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        javafx.scene.layout.HBox.setHgrow(phoneLabel, javafx.scene.layout.Priority.ALWAYS);
        namePhoneRow.getChildren().addAll(nameLabel, phoneLabel);
        javafx.scene.layout.HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);
        namePhoneRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label addressDetail = new Label(pesanan != null && pesanan.getAlamatPesanan() != null ? pesanan.getAlamatPesanan() : "");
        addressDetail.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");
        addressBox.getChildren().addAll(namePhoneRow, addressDetail);

        Label deliveryTitle = new Label("Delivery Option");
        deliveryTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222;");

        VBox deliveryBox = new VBox(3);
        deliveryBox.setStyle("-fx-background-color: #F8F6FF; -fx-border-color: #E0D7FF; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 16 20 16 20;");
        javafx.scene.layout.HBox shippingRow = new javafx.scene.layout.HBox(10);
        Label shippingType = new Label("Regular shipping");
        shippingType.setStyle("-fx-font-size: 18px; -fx-text-fill: #7B61FF; -fx-font-weight: bold;");
        Label shippingPrice = new Label("Rp20.000");
        shippingPrice.setStyle("-fx-font-size: 18px; -fx-text-fill: #7B61FF; -fx-font-weight: bold;");
        shippingPrice.setMaxWidth(Double.MAX_VALUE);
        shippingPrice.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        shippingRow.getChildren().addAll(shippingType, new Label(), shippingPrice);
        javafx.scene.layout.HBox.setHgrow(shippingType, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.HBox.setHgrow(shippingPrice, javafx.scene.layout.Priority.ALWAYS);

        javafx.scene.layout.HBox etaRow = new javafx.scene.layout.HBox(8);
        javafx.scene.control.Label etaIcon = new javafx.scene.control.Label("\u25CF");
        etaIcon.setStyle("-fx-text-fill: #B0B0B0; -fx-font-size: 12px;");
        Label etaText = new Label("Estimated arrival : 2 - 3 Days");
        etaText.setStyle("-fx-font-size: 14px; -fx-text-fill: #B0B0B0;");
        etaRow.getChildren().addAll(etaIcon, etaText);

        deliveryBox.getChildren().addAll(shippingRow, etaRow);

        VBox priceBox = new VBox(10);
        priceBox.setStyle("-fx-background-color: #F8F6FF; -fx-border-color: #E0D7FF; -fx-border-radius: 12; -fx-background-radius: 12; -fx-padding: 18 18 18 18;");

        javafx.scene.layout.HBox subtotalRow = new javafx.scene.layout.HBox();
        Label subtotalLabel = new Label("Subtotal");
        subtotalLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: #888;");
        Label subtotalValue = new Label(String.format("Rp%,d", pesanan.getTotalPesanan()).replace(',', '.'));
        subtotalValue.setStyle("-fx-font-size: 17px; -fx-text-fill: #7B61FF; -fx-font-weight: bold;");
        subtotalValue.setMaxWidth(Double.MAX_VALUE);
        subtotalValue.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        subtotalRow.getChildren().addAll(subtotalLabel, subtotalValue);
        javafx.scene.layout.HBox.setHgrow(subtotalLabel, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.HBox.setHgrow(subtotalValue, javafx.scene.layout.Priority.ALWAYS);

        javafx.scene.layout.HBox shippingCostRow = new javafx.scene.layout.HBox();
        Label shippingCostLabel = new Label("Shipping Cost");
        shippingCostLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: #888;");
        Label shippingCostValue = new Label("Rp20.000");
        shippingCostValue.setStyle("-fx-font-size: 17px; -fx-text-fill: #7B61FF; -fx-font-weight: bold;");
        shippingCostValue.setMaxWidth(Double.MAX_VALUE);
        shippingCostValue.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        shippingCostRow.getChildren().addAll(shippingCostLabel, shippingCostValue);
        javafx.scene.layout.HBox.setHgrow(shippingCostLabel, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.HBox.setHgrow(shippingCostValue, javafx.scene.layout.Priority.ALWAYS);

        priceBox.getChildren().addAll(subtotalRow, shippingCostRow);

        javafx.scene.layout.HBox totalRow = new javafx.scene.layout.HBox();
        Label totalLabel = new Label("Total");
        totalLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #222;");
        int subtotal = (activeProduct != null ? activeProduct.getPrice() * prescriptionQuantity : 0);
        int shipping = 20000;
        Label totalValue = new Label(String.format("Rp%,d", subtotal + shipping).replace(',', '.'));
        totalValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #7B61FF;");
        totalValue.setMaxWidth(Double.MAX_VALUE);
        totalValue.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        totalRow.getChildren().addAll(totalLabel, totalValue);
        javafx.scene.layout.HBox.setHgrow(totalLabel, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.layout.HBox.setHgrow(totalValue, javafx.scene.layout.Priority.ALWAYS);

        javafx.scene.layout.HBox buttonRow = new javafx.scene.layout.HBox(10);
        buttonRow.setAlignment(javafx.geometry.Pos.CENTER);

        javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
        spacer.setMaxWidth(48); 
        javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button cancelBtn = new Button("Cancel");
        Button placeOrderBtn = new Button("Place Order");

        cancelBtn.setPrefWidth(260); 
        placeOrderBtn.setPrefWidth(260);
        cancelBtn.setPrefHeight(52);
        placeOrderBtn.setPrefHeight(52);

        cancelBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #7B61FF; " +
            "-fx-text-fill: #7B61FF; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-font-size: 17px;"
        );
        placeOrderBtn.setStyle(
            "-fx-background-color: #7B61FF; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-font-size: 17px;"
        );

        buttonRow.getChildren().addAll(cancelBtn, spacer, placeOrderBtn);

        popup.getChildren().addAll(
            addressTitle, addressBox,
            deliveryTitle, deliveryBox,
            priceBox, totalRow,
            buttonRow
        );
        overlay.getChildren().add(popup);

        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof javafx.scene.layout.Pane) {
            javafx.scene.layout.Pane pane = (javafx.scene.layout.Pane) root;
            pane.getChildren().add(overlay);
            overlay.toFront();
            cancelBtn.setOnAction(ev -> pane.getChildren().remove(overlay));
            placeOrderBtn.setOnAction(ev -> {
                pane.getChildren().remove(overlay);
                showCheckoutSuccessPopup();
                clearCart();
                prescriptionFilled = false;
            });
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) pane.getChildren().remove(overlay);
            });
        } else {
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().add(root);
            stackPane.getChildren().add(overlay);
            scene.setRoot(stackPane);
            overlay.toFront();
            cancelBtn.setOnAction(ev -> stackPane.getChildren().remove(overlay));
            placeOrderBtn.setOnAction(ev -> {
                stackPane.getChildren().remove(overlay);
                showCheckoutSuccessPopup();
                clearCart();
                prescriptionFilled = false;
            });
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) stackPane.getChildren().remove(overlay);
            });
        }
    }

    private void showCheckoutSuccessPopup() {
        javafx.scene.Scene scene = buttonContainer.getScene();
        if (scene == null) return;

        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setAlignment(javafx.geometry.Pos.CENTER);
        overlay.setPrefWidth(scene.getWidth());
        overlay.setPrefHeight(scene.getHeight());

        VBox popup = new VBox(24);
        popup.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 56px 48px;" +
            "-fx-background-radius: 32;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 28, 0, 0, 14);"
        );
        popup.setMaxWidth(500); 
        popup.setMinWidth(380);
        popup.setAlignment(javafx.geometry.Pos.CENTER);

        Label title = new Label("Checkout Complete");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label message = new Label("Your order has been processed successfully!");
        message.setStyle("-fx-font-size: 18px; -fx-text-fill: #444;");

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(180);
        okBtn.setPrefHeight(52);
        okBtn.setStyle(
            "-fx-background-color: #7B61FF; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-border-radius: 10; " +
            "-fx-font-size: 17px;"
        );

        popup.getChildren().addAll(title, message, okBtn);
        overlay.getChildren().add(popup);

        javafx.scene.Parent root = scene.getRoot();
        if (root instanceof javafx.scene.layout.Pane) {
            javafx.scene.layout.Pane pane = (javafx.scene.layout.Pane) root;
            pane.getChildren().add(overlay);
            overlay.toFront();
            okBtn.setOnAction(ev -> pane.getChildren().remove(overlay));
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) pane.getChildren().remove(overlay);
            });
        } else {
            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
            stackPane.getChildren().add(root);
            stackPane.getChildren().add(overlay);
            scene.setRoot(stackPane);
            overlay.toFront();
            okBtn.setOnAction(ev -> stackPane.getChildren().remove(overlay));
            overlay.setOnMouseClicked(ev -> {
                if (ev.getTarget() == overlay) stackPane.getChildren().remove(overlay);
            });
        }
    }

    public void setUser(com.pekaboo.entities.User user) {
        this.user = user;
    }

    public void setPesanan(Pesanan pesanan) {
        this.pesanan = pesanan;
    }
}
