package com.pekaboo.features.home;

import com.pekaboo.features.navbar.NavbarController;
import com.pekaboo.features.profile.ProfileController;

import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * Controller ini HANYA BERTUGAS sebagai pemuat halaman.
 * Tidak ada logika sesi, login, atau user di sini.
 */
public class MainController {

    @FXML
    private AnchorPane contentArea;
    
    @FXML
    private NavbarController navbarController; 

    private String currentPageCss = null;

    @FXML
    public void initialize() {
        // Menghubungkan dirinya ke NavbarController
        navbarController.setMainController(this);
    }
    
    /**
     * Metode generik untuk memuat FXML dan CSS apa pun yang diberikan.
     */
    public void loadPage(String fxmlPath, String cssPath) {
        try {
            Scene scene = contentArea.getScene();
            if (scene == null) {
                Platform.runLater(() -> loadPage(fxmlPath, cssPath));
                return;
            }

            // Hapus CSS dari halaman sebelumnya
            if (currentPageCss != null && !currentPageCss.isEmpty()) {
                URL oldCssUrl = getClass().getResource(currentPageCss);
                if (oldCssUrl != null) scene.getStylesheets().remove(oldCssUrl.toExternalForm());
                currentPageCss = null;
            }
            
            // Kosongkan area konten
            contentArea.getChildren().clear();

            if (fxmlPath == null || "placeholder".equalsIgnoreCase(fxmlPath)) {
                return; // Jika placeholder, biarkan kosong
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            
            // Logika untuk menghubungkan controller
            Object controller = loader.getController();
            if (controller instanceof ProfileController) {
                ((ProfileController) controller).setMainController(this);
            }
            // Tambahkan controller lain di sini jika diperlukan di masa depan

            // Muat CSS baru jika ada
            if (cssPath != null && !"null".equalsIgnoreCase(cssPath)) {
                URL newCssUrl = getClass().getResource(cssPath);
                if (newCssUrl != null) {
                    scene.getStylesheets().add(newCssUrl.toExternalForm());
                    this.currentPageCss = cssPath;
                }
            }
            
            // Tampilkan halaman baru
            contentArea.getChildren().add(page);
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (Exception e) {
            System.err.println("Gagal memuat halaman: " + fxmlPath);
            e.printStackTrace();
        }
    }
}