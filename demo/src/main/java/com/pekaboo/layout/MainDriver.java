package com.pekaboo.layout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

/**
 * INI ADALAH DRIVER KHUSUS UNTUK TESTING FRONTEND.
 * Jalankan file ini langsung dari IDE (klik tombol "Run") untuk melihat
 * layout utama (Main.fxml) tanpa harus mengubah App.java.
 */
public class MainDriver extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);
            loadStylesheet(scene, "/com/pekaboo/styles/Navbar.css");
            loadStylesheet(scene, "/com/pekaboo/styles/global-styles.css");

            primaryStage.setTitle("Frontend Test Driver - Main Layout");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Gagal memuat FXML untuk driver.");
            e.printStackTrace();
        }
    }

    private void loadStylesheet(Scene scene, String path) {
        URL cssUrl = getClass().getResource(path);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Peringatan: Stylesheet tidak ditemukan di -> " + path);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
