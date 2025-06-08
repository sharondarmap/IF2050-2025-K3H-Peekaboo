package com.pekaboo.features.pembelian;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.pekaboo.entities.Pesanan;
import com.pekaboo.entities.Product;
import com.pekaboo.entities.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CheckoutDriver extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Dummy list of products
        List<Product> products = Arrays.asList(
            new Product(1, 50, 150000, "Kaos Polos", "Uniqlo", "Hitam", "L", "/com/pekaboo/pembelian/assets/gambar1.png"),
            new Product(2, 30, 200000, "Jaket Hoodie", "H&M", "Orange", "M", "/com/pekaboo/pembelian/assets/gambar2.png"),
            new Product(3, 20, 100000, "Topi Baseball", "Adidas", "Merah", "All Size", "/com/pekaboo/pembelian/assets/gambar3.png")
        );

        // Dummy list of users
        List<User> users = Arrays.asList(
            new User(1, "Nadya Rahma", "pass123", "nadya@email.com", "0812-0001-2345", LocalDate.of(2022, 5, 10), "Perempuan", "Jl. Sukabirus No. 15", "active"),
            new User(2, "Fajar Maulana", "fajar456", "fajar@email.com", "0857-9876-5432", LocalDate.of(2021, 8, 21), "Laki-laki", "Jl. Cibiru Hilir No. 7", "active")
        );

        // Dummy list of orders/pesanan (1 pesanan untuk produk ke-2 oleh user ke-1)
        List<Pesanan> pesananList = Arrays.asList(
            new Pesanan(1, 200000, "2023-10-01", "Jl. Sukabirus No. 15", users.get(1).getIdUser(), products.get(2).getId()),
            new Pesanan(2, 150000, "2023-10-02", "Jl. Cibiru Hilir No. 7", users.get(1).getIdUser(), products.get(0).getId())
        );        
        
        Product selectedProduct = products.get(1); // misal produk ke-2
        User selectedUser = users.get(0);          // misal user ke-1
        Pesanan selectedPesanan = pesananList.get(0); // misal pesanan ke-1

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pekaboo/pembelian/checkout.fxml"));
        loader.setControllerFactory(clazz -> {
            try {
                Object ctrl = clazz.getDeclaredConstructor().newInstance();
                if (ctrl instanceof CheckoutController) {
                    CheckoutController checkoutCtrl = (CheckoutController) ctrl;
                    checkoutCtrl.setActiveProduct(selectedProduct);
                    checkoutCtrl.setUser(selectedUser);
                    checkoutCtrl.setPesanan(selectedPesanan);
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
