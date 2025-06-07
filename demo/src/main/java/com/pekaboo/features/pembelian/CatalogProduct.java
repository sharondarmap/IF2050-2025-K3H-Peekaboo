package com.pekaboo.features.pembelian;

import com.pekaboo.entities.Product;
import com.pekaboo.repositories.ProductRepo;
import com.pekaboo.repositories.postgres.PostgresProdukRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CatalogProduct implements Initializable {
    @FXML private GridPane gridPane;

    // Use the actual PostgreSQL-backed repository
    private final ProductRepo productRepo = new PostgresProdukRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCatalogProduct(productRepo.getAllProduct());
    }

    public void showCatalogProduct(List<Product> allProduct) {
        gridPane.getChildren().clear();
        int column = 0;
        int row = 0;

        for (Product product : allProduct) {
            VBox card = new VBox();
            card.getStyleClass().add("card");

            ImageView imageView;
            try {
                // If imagePath is stored as absolute or file path, load accordingly
                imageView = new ImageView(new Image("file:" + product.getImagePath()));
            } catch (Exception e) {
                // Fallback image in case of error or null
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/assets/img/placeholder.png")));
            }

            imageView.setFitWidth(160);
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

            GridPane.setMargin(card, new Insets(10));
            gridPane.add(card, column, row);
            column++;

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    public void showProductDetail(Product product) {
        // TODO: Detail produk (misal popup atau navigasi ke halaman detail dari halaman Nakei)
    }

    public List<Product> catalogFilter(String keyword) {
        return productRepo.getAllProduct().stream()
            .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}
