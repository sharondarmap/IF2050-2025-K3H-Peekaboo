package com.pekaboo.features.pembelian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class CatalogDriver extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pekaboo/pembelian/CatalogProduct.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            String catalogCss = getClass().getResource("/com/pekaboo/pembelian/catalog.css").toExternalForm();
            scene.getStylesheets().add(catalogCss);
            primaryStage.setTitle("Test Driver - Katalog Produk");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat FXML untuk driver.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
