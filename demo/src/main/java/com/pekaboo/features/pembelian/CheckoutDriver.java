package com.pekaboo.features.pembelian;

import com.pekaboo.entities.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CheckoutDriver extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Product> dummyProducts = Arrays.asList(
            new Product(1, 50, 150000, "Kaos Polos", "Uniqlo", "Hitam", "L", "/com/pekaboo/pembelian/assets/gambar1.png"),
            new Product(2, 30, 200000, "Jaket Hoodie", "H&M", "Orange", "M", "/com/pekaboo/pembelian/assets/gambar2.png"),
            new Product(3, 20, 100000, "Topi Baseball", "Adidas", "Merah", "All Size", "/com/pekaboo/pembelian/assets/gambar3.png")
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pekaboo/pembelian/checkout.fxml"));
        loader.setControllerFactory(clazz -> {
            try {
                Object ctrl = clazz.getDeclaredConstructor().newInstance();
                if (ctrl instanceof CheckoutController) {
                    ((CheckoutController) ctrl).setActiveProduct(dummyProducts.get(1)); // ini ambil dari urutan indeks dummy yang atas hehehhehhehehehhehehehe
                }
                return ctrl;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Parent root = loader.load();
        primaryStage.setTitle("Checkout");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
