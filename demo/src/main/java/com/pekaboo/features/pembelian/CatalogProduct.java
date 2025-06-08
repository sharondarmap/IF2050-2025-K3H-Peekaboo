package com.pekaboo.features.pembelian;

import com.pekaboo.entities.Product;
import com.pekaboo.repositories.MockProductRepo;
import com.pekaboo.repositories.ProductRepo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CatalogProduct implements Initializable {
    
    @FXML
    private Button product01Button;
    @FXML
    private Button product02Button;
    @FXML
    private Button product03Button;
    @FXML
    private Button product04Button;
    @FXML
    private Button product05Button;
    @FXML
    private Button product06Button;
    @FXML
    private Button product07Button;
    @FXML
    private Button product08Button;
    @FXML
    private Button product09Button;
    @FXML
    private Button product10Button;
    @FXML
    private Button product11Button;
    @FXML
    private Button product12Button;
    @FXML
    private Button product13Button;
    @FXML
    private Button product14Button;
    @FXML
    private Button product15Button;
    @FXML
    private Button product16Button;
    @FXML
    private Button product17Button;
    @FXML
    private Button product18Button;
    @FXML
    private Button product19Button;

    @FXML
    private TextField searchTextField;
    @FXML 
    private Button searchButton;
    @FXML
    private TextField minPriceTextField;
    @FXML
    private TextField maxPriceTextField;
    @FXML
    private Button applyfilterButton;
    @FXML
    private Button cancelFilterButton;
    @FXML
    private CheckBox brandCheckBoxHMIF;
    @FXML
    private CheckBox brandCheckBoxASSIST;
    @FXML
    private CheckBox brandCheckBoxIF;
    @FXML
    private CheckBox brandCheckBoxSTI;
    @FXML
    private Button sizeSButton;
    @FXML
    private Button sizeMButton;
    @FXML
    private Button sizeLButton;
    @FXML
    private Button colorRedButton;
    @FXML
    private Button colorOrangeButton;
    @FXML
    private Button colorYellowButton;
    @FXML 
    private Button colorGreenButton;
    @FXML 
    private Button colorBlueButton;
    @FXML 
    private Button colorPurpleButton;
    @FXML 
    private Button colorBrownButton;
    @FXML 
    private Button colorBlackButton;

    @FXML private GridPane gridPane;
    private ProductRepo productRepo = new MockProductRepo();
    private List<Product> allProducts;

    private String selectedColor = null;
    private String selectedSize = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
 // --- Menambahkan aksi untuk semua tombol filter ---
        applyfilterButton.setOnAction(e -> applyFilters());
        searchButton.setOnAction(e -> applyFilters());

        this.allProducts = productRepo.getAllProduct();
        showCatalogProduct(this.allProducts);
        
        // Menambahkan listener agar filter berjalan saat mengetik
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Aksi untuk checkbox
        brandCheckBoxHMIF.setOnAction(e -> applyFilters());
        brandCheckBoxASSIST.setOnAction(e -> applyFilters());
        brandCheckBoxIF.setOnAction(e -> applyFilters());
        brandCheckBoxSTI.setOnAction(e -> applyFilters());
        
        // Aksi untuk tombol cancel
        cancelFilterButton.setOnAction(e -> {
            minPriceTextField.clear();
            maxPriceTextField.clear();
            showCatalogProduct(this.allProducts);
        });

        applyfilterButton.setOnAction(e -> applyFilters());

        // Setup untuk tombol warna dan ukuran
        setupFilterButton(sizeSButton, "S");
        setupFilterButton(sizeMButton, "M");
        setupFilterButton(sizeLButton, "L");

        setupColorButton(colorRedButton, "Merah");
        setupColorButton(colorOrangeButton, "Oranye");
        setupColorButton(colorYellowButton, "Kuning");
        setupColorButton(colorGreenButton, "Hijau");
        setupColorButton(colorBlueButton, "Biru");
        setupColorButton(colorPurpleButton, "Ungu");
        setupColorButton(colorBrownButton, "Coklat");
        setupColorButton(colorBlackButton, "Hitam");
    }

    public void showCatalogProduct(List<Product> allProduct) {
        gridPane.getChildren().clear();
        int column = 0;
        int row = 0;

        for (Product product : allProduct) {
            VBox card = new VBox();
            card.getStyleClass().add("card-content");

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(product.getImagePath())));
            imageView.setFitWidth(280);
            imageView.setPreserveRatio(true);
            imageView.getStyleClass().add("card-image");

            VBox cardBody = new VBox();
            cardBody.getStyleClass().add("card-body");

            Label nameLabel = new Label(product.getName());
            nameLabel.getStyleClass().add("card-title");

            Label brandLabel = new Label(product.getBrand());
            brandLabel.getStyleClass().add("card-subtitle");

            Label priceLabel = new Label("Rp" + product.getPrice());
            priceLabel.getStyleClass().add("card-price");

            cardBody.getChildren().addAll(nameLabel, brandLabel, priceLabel);
            card.getChildren().addAll(imageView, cardBody);

            Button productButton = new Button();
            productButton.setGraphic(card);
            productButton.getStyleClass().add("card-button");
            productButton.setOnAction(event -> handleProductClick(product));

            GridPane.setMargin(productButton, new Insets(10));
            gridPane.add(productButton, column, row);
            column++;

            if (column == 3) { 
                column = 0;
                row++;
            }

        }
    }

    private void applyFilters() {
        List<Product> filteredList = this.allProducts.stream()
            // 1. Filter berdasarkan Keyword Pencarian
            .filter(p -> {
                String keyword = searchTextField.getText();
                if (keyword == null || keyword.trim().isEmpty()) return true;
                String lowerKeyword = keyword.toLowerCase();
                return p.getName().toLowerCase().contains(lowerKeyword) ||
                       p.getBrand().toLowerCase().contains(lowerKeyword);
            })
            // 2. Filter berdasarkan Harga Minimum
            .filter(p -> {
                try {
                    String minText = minPriceTextField.getText().replaceAll("[^\\d]", "");
                    if (minText.isEmpty()) return true;
                    double minPrice = Double.parseDouble(minText);
                    return p.getPrice() >= minPrice;
                } catch (NumberFormatException e) {
                    return true;
                }
            })
            // 3. Filter berdasarkan Harga Maksimum
            .filter(p -> {
                try {
                    String maxText = maxPriceTextField.getText().replaceAll("[^\\d]", "");
                    if (maxText.isEmpty()) return true;
                    double maxPrice = Double.parseDouble(maxText);
                    return p.getPrice() <= maxPrice;
                } catch (NumberFormatException e) {
                    return true;
                }
            })
            // 4. Filter berdasarkan Brand (Checkbox)
            .filter(p -> {
                List<String> selectedBrands = new ArrayList<>();
                if (brandCheckBoxHMIF.isSelected()) selectedBrands.add("HMIF");
                if (brandCheckBoxASSIST.isSelected()) selectedBrands.add("ASSIST");
                if (brandCheckBoxIF.isSelected()) selectedBrands.add("IF");
                if (brandCheckBoxSTI.isSelected()) selectedBrands.add("STI");
                return selectedBrands.isEmpty() || selectedBrands.contains(p.getBrand());
            })
            // 5. Filter berdasarkan Warna
            .filter(p -> selectedColor == null || p.getColor().equalsIgnoreCase(selectedColor))
            // 6. Filter berdasarkan Ukuran
            .filter(p -> selectedSize == null || p.getSize().equalsIgnoreCase(selectedSize))
            .collect(Collectors.toList());

        showCatalogProduct(filteredList);
    }

        private void setupColorButton(Button button, String color) {
        button.setOnAction(e -> {
            // Jika warna yang sama diklik lagi, batalkan filter
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
    
        colorRedButton.setStyle(colorRedButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorOrangeButton.setStyle(colorOrangeButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorYellowButton.setStyle(colorYellowButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorGreenButton.setStyle(colorGreenButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorBlueButton.setStyle(colorBlueButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorPurpleButton.setStyle(colorPurpleButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorBrownButton.setStyle(colorBrownButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        colorBlackButton.setStyle(colorBlackButton.getStyle().replaceAll("-fx-border-color: #5B36C9; -fx-border-width: 2;", ""));
        
        // Terapkan style aktif pada yang dipilih
        String activeStyle = "-fx-border-color: #5B36C9; -fx-border-width: 2;";
        if ("Merah".equals(selectedColor)) colorRedButton.setStyle(colorRedButton.getStyle() + activeStyle);
        if ("Oranye".equals(selectedColor)) colorOrangeButton.setStyle(colorOrangeButton.getStyle() + activeStyle);
        if ("Kuning".equals(selectedColor)) colorYellowButton.setStyle(colorYellowButton.getStyle() + activeStyle);
        if ("Hijau".equals(selectedColor)) colorGreenButton.setStyle(colorGreenButton.getStyle() + activeStyle);
        if ("Biru".equals(selectedColor)) colorBlueButton.setStyle(colorBlueButton.getStyle() + activeStyle);
        if ("Ungu".equals(selectedColor)) colorPurpleButton.setStyle(colorPurpleButton.getStyle() + activeStyle);
        if ("Coklat".equals(selectedColor)) colorBrownButton.setStyle(colorBrownButton.getStyle() + activeStyle);
        if ("Hitam".equals(selectedColor)) colorBlackButton.setStyle(colorBlackButton.getStyle() + activeStyle);
    }

    private void updateSizeButtonStyles() {
        // Reset semua style dulu
        sizeSButton.getStyleClass().remove("size-button-active");
        sizeMButton.getStyleClass().remove("size-button-active");
        sizeLButton.getStyleClass().remove("size-button-active");

        // Terapkan style aktif
        if ("S".equals(selectedSize)) sizeSButton.getStyleClass().add("size-button-active");
        if ("M".equals(selectedSize)) sizeMButton.getStyleClass().add("size-button-active");
        if ("L".equals(selectedSize)) sizeLButton.getStyleClass().add("size-button-active");
    }

    private void handleProductClick(Product product) {
        showProductDetail(product);
    }

    public void showProductDetail(Product product) {
        // TODO: Implementasi detail produk (misal popup atau navigasi ke halaman detail)
    }

}
