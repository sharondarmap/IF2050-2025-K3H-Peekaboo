package com.pekaboo.layout;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {
    private MainController mainController;

    @FXML
    private Button homeButton;
    @FXML
    private Button reservationButton;
    @FXML
    private Button catalogButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button historyButton;
    @FXML
    private ImageView profileImageView;
    @FXML
    private ImageView logoImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image logo = new Image(getClass().getResourceAsStream("/com/pekaboo/images/logo.png"));
            logoImageView.setImage(logo);

            Image profileIcon = new Image(getClass().getResourceAsStream("/com/pekaboo/images/ProfileIcon.png"));
            profileImageView.setImage(profileIcon);
            
        } catch (Exception e) {
            System.err.println("FATAL: Gagal memuat gambar logo atau profile. Pastikan file ada di resources/com/pekaboo/images/");
            e.printStackTrace();
        }
    }    

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        handleNavigation(new ActionEvent(homeButton, null));
    }

    /**
     * Method untuk menangani navigasi ketika tombol navbar diklik.
     * @param event Event yang terjadi saat tombol diklik.
     */
    @FXML
    void handleNavigation(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        ObservableList<?> userData = (ObservableList<?>) clickedButton.getUserData();

        if (userData != null && !userData.isEmpty()) {
            String fxmlPath = (String) userData.get(0);
            String cssPath = (userData.size() > 1) ? (String) userData.get(1) : "null";
            
            // Cek jika path adalah placeholder, maka kirim null untuk kosongkan halaman
            if ("placeholder".equalsIgnoreCase(fxmlPath)) {
                mainController.loadPage(null, null);
            } else {
                 // Jika path CSS adalah string "null", ubah menjadi null sungguhan
                if ("null".equalsIgnoreCase(cssPath)) {
                    cssPath = null;
                }
                mainController.loadPage(fxmlPath, cssPath);
            }

            setActiveButton(clickedButton);
        }
    } 

/* 
    @FXML
    void handleHomeClick(ActionEvent event) {
        System.out.println("Tombol Home diklik!");
        mainController.loadPage("/com/pekaboo/features/home/Home.fxml");
        setActiveButton(homeButton);

    }

    @FXML
    void handleReservationClick(ActionEvent event) {
        System.out.println("Tombol Reservation diklik!");
        // mainController.loadPage("/com/pekaboo/features/pembelian/Reservation.fxml");
        // setActiveButton("Reservation");
    }

    @FXML
    void handleCatalogClick(ActionEvent event) {
        System.out.println("Tombol Catalog diklik!");
        mainController.loadPage("/com/pekaboo/features/pembelian/CatalogProduct.fxml");
        setActiveButton(catalogButton);
    }

    @FXML
    void handleLoginClick(ActionEvent event) {
        System.out.println("Tombol Login diklik!");
        // mainController.loadPage("/com/pekaboo/features/auth/Login.fxml");
        // setActiveButton("Login");
    }

    @FXML
    void handleHistoryClick(ActionEvent event) {
        System.out.println("Tombol History diklik!");
        // mainController.loadPage("/com/pekaboo/features/pembelian/History.fxml");
        // setActiveButton("History");
    }


*/
    @FXML
    void handleProfileClick(MouseEvent event) {
        System.out.println("Tombol Profile diklik!");
        mainController.loadPage(null, null);
        setActiveButton(null);
        // mainController.loadPage("/com/pekaboo/features/profile/Profile.fxml");
        // setActiveButton("Profile");
    }
    
    public void setActiveButton(Button activeButton) {
        homeButton.getStyleClass().remove("active");
        reservationButton.getStyleClass().remove("active");
        catalogButton.getStyleClass().remove("active");
        historyButton.getStyleClass().remove("active");
        
        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
    }
}