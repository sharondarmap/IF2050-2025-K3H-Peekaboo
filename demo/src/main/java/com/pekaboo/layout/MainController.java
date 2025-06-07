package com.pekaboo.layout;

//import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class MainController {

    @FXML
    private AnchorPane contentArea;
    
    @FXML
    private NavbarController navbarController; 

    private String currentPageCss = null;

    @FXML
    public void initialize() {
        navbarController.setMainController(this);
    }

    public void loadPage(String fxmlPath, String cssPath) {
        try {
            Scene scene = contentArea.getScene();
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
            
            contentArea.getChildren().clear();

            if (fxmlPath == null || "placeholder".equalsIgnoreCase(fxmlPath)) {
                return;
            }

            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));

            if (cssPath != null && !"null".equalsIgnoreCase(cssPath)) {
                URL newCssUrl = getClass().getResource(cssPath);
                if (newCssUrl != null) {
                    scene.getStylesheets().add(newCssUrl.toExternalForm());
                    this.currentPageCss = cssPath;
                }
            }
            
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