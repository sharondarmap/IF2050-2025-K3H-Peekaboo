package com.pekaboo.layout;

import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private NavbarController navbarController;

    private String currentPageCss = null;

    @FXML
    public void initialize() {
        navbarController.setMainController(this);
    }

    /**
     * Memuat halaman FXML dan CSS yang sesuai.
     * @param fxmlPath Path ke file FXML yang akan dimuat.
     * @param cssPath Path ke file CSS yang akan diterapkan pada halaman.
     */
    public void loadPage(String fxmlPath, String cssPath) {
        try {
            Scene scene = mainPane.getScene();
            // Jika scene belum siap, tunggu sebentar.
            if (scene == null) {
                Platform.runLater(() -> loadPage(fxmlPath, cssPath));
                return;
            }

            if (currentPageCss != null && !currentPageCss.isEmpty()) {
                URL oldCssUrl = getClass().getResource(currentPageCss);
                if (oldCssUrl != null) {
                    scene.getStylesheets().remove(oldCssUrl.toExternalForm());
                }
                currentPageCss = null; 
            }

            if (fxmlPath == null || "placeholder".equalsIgnoreCase(fxmlPath)) {
                mainPane.setCenter(null); 
                return; 
            }

            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));

            if (cssPath != null && !"null".equalsIgnoreCase(cssPath)) {
                URL newCssUrl = getClass().getResource(cssPath);
                if (newCssUrl != null) {
                    scene.getStylesheets().add(newCssUrl.toExternalForm());
                    this.currentPageCss = cssPath; // Simpan path CSS yang baru
                } else {
                    System.err.println("Peringatan: File CSS tidak ditemukan di " + cssPath);
                }
            }
            
            mainPane.setCenter(page);

        } catch (Exception e) {
            System.err.println("Gagal total memuat halaman: " + fxmlPath);
            e.printStackTrace();
        }
    }
}