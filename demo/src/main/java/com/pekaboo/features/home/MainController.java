package com.pekaboo.features.home;

//import java.io.IOException;
import java.net.URL;

import com.pekaboo.PrimaryController;
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
        navbarController.setMainController(this);

        // Add global navbar CSS once
        Scene scene = contentArea.getScene();
        if (scene != null) {
            URL navbarCss = getClass().getResource("/com/pekaboo/navbar/navbar.css");
            if (navbarCss != null) {
                scene.getStylesheets().add(navbarCss.toExternalForm());
            }
        } else {
            Platform.runLater(() -> {
                Scene laterScene = contentArea.getScene();
                if (laterScene != null) {
                    URL navbarCss = getClass().getResource("/com/pekaboo/navbar/navbar.css");
                    if (navbarCss != null) {
                        laterScene.getStylesheets().add(navbarCss.toExternalForm());
                    }
                }
            });
        }
    }

    public void loadPage(String fxmlPath, String cssPath) {
        System.out.println(">> Memuat halaman: " + fxmlPath + " dengan CSS: " + cssPath);
        try {
            Scene scene = contentArea.getScene();
            if (scene == null) {
                Platform.runLater(() -> loadPage(fxmlPath, cssPath));
                return;
            }

            if (currentPageCss != null && !currentPageCss.isEmpty()) {
                URL oldCssUrl = getClass().getResource(currentPageCss);
                if (oldCssUrl != null) scene.getStylesheets().remove(oldCssUrl.toExternalForm());
                currentPageCss = null;
            }
            
            contentArea.getChildren().clear();

            if (fxmlPath == null || "placeholder".equalsIgnoreCase(fxmlPath)) {
                return; 
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();

            // Inject mainController if applicable
            Object controller = loader.getController();
            if (controller instanceof PrimaryController) {
                ((PrimaryController) controller).setMainController(this);
            }

            if (cssPath != null && !"null".equalsIgnoreCase(cssPath)) {
                URL newCssUrl = getClass().getResource(cssPath);
                if (newCssUrl != null) {
                    scene.getStylesheets().add(newCssUrl.toExternalForm());
                    this.currentPageCss = cssPath;
                }
            }
            // URL navbarCss = getClass().getResource("/com/pekaboo/navbar/navbar.css");
            // if (navbarCss != null) {
            //     scene.getStylesheets().add(navbarCss.toExternalForm());
            // } else {
            //     System.err.println("⚠️ navbar.css not found!");
            // }
            
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
