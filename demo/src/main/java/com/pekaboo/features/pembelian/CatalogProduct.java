package com.pekaboo.features.pembelian;

import com.pekaboo.entities.Product;
import com.pekaboo.repositories.ProductRepo;
import com.pekaboo.repositories.postgres.PostgresProdukRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;

public class CatalogProduct implements Initializable {
    @FXML private GridPane gridPane;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private TextField minPriceTextField;
    @FXML private TextField maxPriceTextField;
    @FXML private Button applyfilterButton;
    @FXML private Button cancelFilterButton;
    @FXML private CheckBox brandCheckBoxHMIF;
    @FXML private CheckBox brandCheckBoxASSIST;
    @FXML private CheckBox brandCheckBoxIF;
    @FXML private CheckBox brandCheckBoxSTI;
    @FXML private Button sizeSButton;
    @FXML private Button sizeMButton;
    @FXML private Button sizeLButton;
    @FXML private Button colorRedButton;
    @FXML private Button colorOrangeButton;
    @FXML private Button colorYellowButton;
    @FXML private Button colorGreenButton;
    @FXML private Button colorBlueButton;
    @FXML private Button colorPurpleButton;
    @FXML private Button colorBrownButton;
    @FXML private Button colorBlackButton;

    private final ProductRepo productRepo = new PostgresProdukRepository();
    private List<Product> allProducts;
    private String selectedColor = null;
    private String selectedSize = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allProducts = productRepo.getAllProduct();
        showCatalogProduct(allProducts);

        // Search functionality
        searchButton.setOnAction(e -> applyFilters());
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Filter buttons
        if (applyfilterButton != null) applyfilterButton.setOnAction(e -> applyFilters());
        if (cancelFilterButton != null) {
            cancelFilterButton.setOnAction(e -> {
                if (minPriceTextField != null) minPriceTextField.clear();
                if (maxPriceTextField != null) maxPriceTextField.clear();
                selectedColor = null;
                selectedSize = null;
                updateColorButtonStyles();
                updateSizeButtonStyles();
                if (brandCheckBoxHMIF != null) brandCheckBoxHMIF.setSelected(false);
                if (brandCheckBoxASSIST != null) brandCheckBoxASSIST.setSelected(false);
                if (brandCheckBoxIF != null) brandCheckBoxIF.setSelected(false);
                if (brandCheckBoxSTI != null) brandCheckBoxSTI.setSelected(false);
                showCatalogProduct(allProducts);
            });
        }

        // Brand checkboxes
        if (brandCheckBoxHMIF != null) brandCheckBoxHMIF.setOnAction(e -> applyFilters());
        if (brandCheckBoxASSIST != null) brandCheckBoxASSIST.setOnAction(e -> applyFilters());
        if (brandCheckBoxIF != null) brandCheckBoxIF.setOnAction(e -> applyFilters());
        if (brandCheckBoxSTI != null) brandCheckBoxSTI.setOnAction(e -> applyFilters());

        // Size buttons
        if (sizeSButton != null) setupFilterButton(sizeSButton, "S");
        if (sizeMButton != null) setupFilterButton(sizeMButton, "M");
        if (sizeLButton != null) setupFilterButton(sizeLButton, "L");

        // Color buttons
        if (colorRedButton != null) setupColorButton(colorRedButton, "Merah");
        if (colorOrangeButton != null) setupColorButton(colorOrangeButton, "Oranye");
        if (colorYellowButton != null) setupColorButton(colorYellowButton, "Kuning");
        if (colorGreenButton != null) setupColorButton(colorGreenButton, "Hijau");
        if (colorBlueButton != null) setupColorButton(colorBlueButton, "Biru");
        if (colorPurpleButton != null) setupColorButton(colorPurpleButton, "Ungu");
        if (colorBrownButton != null) setupColorButton(colorBrownButton, "Coklat");
        if (colorBlackButton != null) setupColorButton(colorBlackButton, "Hitam");
    }

    private void applyFilters() {
        List<Product> filteredList = allProducts.stream()
            //Filter by search keyword
            .filter(p -> {
                String keyword = searchTextField.getText();
                if (keyword == null || keyword.trim().isEmpty()) return true;
                String lowerKeyword = keyword.toLowerCase();
                return p.getName().toLowerCase().contains(lowerKeyword) ||
                       p.getBrand().toLowerCase().contains(lowerKeyword);
            })
            //Filter by minimum price
            .filter(p -> {
                if (minPriceTextField == null) return true;
                try {
                    String minText = minPriceTextField.getText().replaceAll("[^\\d]", "");
                    if (minText.isEmpty()) return true;
                    double minPrice = Double.parseDouble(minText);
                    return p.getPrice() >= minPrice;
                } catch (NumberFormatException e) {
                    return true;
                }
            })
            //Filter by maximum price
            .filter(p -> {
                if (maxPriceTextField == null) return true;
                try {
                    String maxText = maxPriceTextField.getText().replaceAll("[^\\d]", "");
                    if (maxText.isEmpty()) return true;
                    double maxPrice = Double.parseDouble(maxText);
                    return p.getPrice() <= maxPrice;
                } catch (NumberFormatException e) {
                    return true;
                }
            })
            //Filter by brand checkboxes
            .filter(p -> {
                List<String> selectedBrands = new ArrayList<>();
                if (brandCheckBoxHMIF != null && brandCheckBoxHMIF.isSelected()) selectedBrands.add("HMIF");
                if (brandCheckBoxASSIST != null && brandCheckBoxASSIST.isSelected()) selectedBrands.add("ASSIST");
                if (brandCheckBoxIF != null && brandCheckBoxIF.isSelected()) selectedBrands.add("IF");
                if (brandCheckBoxSTI != null && brandCheckBoxSTI.isSelected()) selectedBrands.add("STI");
                return selectedBrands.isEmpty() || selectedBrands.contains(p.getBrand());
            })
            //Filter by color
            .filter(p -> selectedColor == null || p.getColor().equalsIgnoreCase(selectedColor))
            //Filter by size
            .filter(p -> selectedSize == null || p.getSize().equalsIgnoreCase(selectedSize))
            .collect(Collectors.toList());

        showCatalogProduct(filteredList);
    }

    public void showCatalogProduct(List<Product> products) {
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER); //Center the grid
        
        int column = 0;
        int row = 0;

        for (Product product : products) {
            // Create the main card container
            VBox card = new VBox();
            card.getStyleClass().add("card-content");
            card.setAlignment(Pos.CENTER); // Center content within card

            // Create and setup the image with proper centering and fitting
            ImageView imageView;
            try {
                String path = "/com/pekaboo/pembelian/assets/" + product.getId() + ".jpg";
                imageView = new ImageView(new Image(getClass().getResourceAsStream(path)));
            } catch (Exception e) {
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/pekaboo/pembelian/assets/5.jpg")));
            }

            // Set image dimensions and properties for proper fitting
            imageView.setFitWidth(280);
            imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.getStyleClass().add("card-image");

            // Create image container with centered alignment and clipping
            StackPane imageContainer = new StackPane(imageView);
            imageContainer.setPrefSize(280, 160);
            imageContainer.setMaxSize(280, 160);
            imageContainer.setMinSize(280, 160);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setStyle("-fx-background-radius: 12 12 0 0; -fx-background-color: #f5f5f5;");
            
            // Apply clipping for rounded corners
            Rectangle clip = new Rectangle(280, 160);
            clip.setArcWidth(12);
            clip.setArcHeight(12);
            imageContainer.setClip(clip);

            // Create the card body
            VBox cardBody = new VBox();
            cardBody.getStyleClass().add("card-body");
            cardBody.setAlignment(Pos.CENTER); // Center the text content

            // Create labels with proper styling
            Label nameLabel = new Label(product.getName());
            nameLabel.getStyleClass().add("card-title");
            nameLabel.setAlignment(Pos.CENTER);

            Label brandLabel = new Label(product.getBrand());
            brandLabel.getStyleClass().add("card-subtitle");
            brandLabel.setAlignment(Pos.CENTER);

            // Fix the price formatting issue by converting int to double
            double priceValue = (double) product.getPrice();
            Label priceLabel = new Label("Rp" + String.format("%,.0f", priceValue));
            priceLabel.getStyleClass().add("card-price");

            // Create HBox to position price label to the center (or right if preferred)
            HBox priceContainer = new HBox();
            priceContainer.setAlignment(Pos.CENTER); // Changed from CENTER_RIGHT to CENTER
            priceContainer.getChildren().add(priceLabel);

            // Add all elements to card body
            cardBody.getChildren().addAll(nameLabel, brandLabel, priceContainer);
            
            // Add image container and body to main card
            card.getChildren().addAll(imageContainer, cardBody);

            // Create the clickable button that wraps the card
            Button productButton = new Button();
            productButton.setGraphic(card);
            productButton.getStyleClass().add("card-button");
            productButton.setOnAction(event -> handleProductClick(product));

            // Add to grid with margin and center alignment
            GridPane.setMargin(productButton, new Insets(10));
            GridPane.setHalignment(productButton, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(productButton, javafx.geometry.VPos.CENTER);
            gridPane.add(productButton, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private void setupColorButton(Button button, String color) {
        button.setOnAction(e -> {
            // If same color clicked again, cancel filter
            selectedColor = color.equals(selectedColor) ? null : color;
            updateColorButtonStyles();
            applyFilters();
        });
    }

    private void setupFilterButton(Button button, String size) {
        button.setOnAction(e -> {
            selectedSize = size.equals(selectedSize) ? null : size;
            updateSizeButtonStyles();
            applyFilters();
        });
    }

    private void updateColorButtonStyles() {
        if (colorRedButton == null) return;
        
        // Reset all color button styles
        colorRedButton.setStyle(colorRedButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorOrangeButton != null) colorOrangeButton.setStyle(colorOrangeButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorYellowButton != null) colorYellowButton.setStyle(colorYellowButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorGreenButton != null) colorGreenButton.setStyle(colorGreenButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorBlueButton != null) colorBlueButton.setStyle(colorBlueButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorPurpleButton != null) colorPurpleButton.setStyle(colorPurpleButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorBrownButton != null) colorBrownButton.setStyle(colorBrownButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        if (colorBlackButton != null) colorBlackButton.setStyle(colorBlackButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        
        // Apply active style to selected color
        String activeStyle = "-fx-border-color: #5B36C9; -fx-border-width: 2; -fx-border-radius: 2;";
        if ("Merah".equals(selectedColor)) colorRedButton.setStyle(colorRedButton.getStyle() + activeStyle);
        if ("Oranye".equals(selectedColor) && colorOrangeButton != null) colorOrangeButton.setStyle(colorOrangeButton.getStyle() + activeStyle);
        if ("Kuning".equals(selectedColor) && colorYellowButton != null) colorYellowButton.setStyle(colorYellowButton.getStyle() + activeStyle);
        if ("Hijau".equals(selectedColor) && colorGreenButton != null) colorGreenButton.setStyle(colorGreenButton.getStyle() + activeStyle);
        if ("Biru".equals(selectedColor) && colorBlueButton != null) colorBlueButton.setStyle(colorBlueButton.getStyle() + activeStyle);
        if ("Ungu".equals(selectedColor) && colorPurpleButton != null) colorPurpleButton.setStyle(colorPurpleButton.getStyle() + activeStyle);
        if ("Coklat".equals(selectedColor) && colorBrownButton != null) colorBrownButton.setStyle(colorBrownButton.getStyle() + activeStyle);
        if ("Hitam".equals(selectedColor) && colorBlackButton != null) colorBlackButton.setStyle(colorBlackButton.getStyle() + activeStyle);
    }

    private void updateSizeButtonStyles() {
        if (sizeSButton == null) return;
        
        // Reset all size button styles
        sizeSButton.getStyleClass().remove("size-button-active");
        if (sizeMButton != null) sizeMButton.getStyleClass().remove("size-button-active");
        if (sizeLButton != null) sizeLButton.getStyleClass().remove("size-button-active");

        // Apply active style to selected size
        if ("S".equals(selectedSize)) sizeSButton.getStyleClass().add("size-button-active");
        if ("M".equals(selectedSize) && sizeMButton != null) sizeMButton.getStyleClass().add("size-button-active");
        if ("L".equals(selectedSize) && sizeLButton != null) sizeLButton.getStyleClass().add("size-button-active");
    }

    private void handleProductClick(Product product) {
        // Handle product click - navigate to detail page or show popup
        System.out.println("Clicked on product: " + product.getName());
        // TODO: Implement product detail navigation
    }
}