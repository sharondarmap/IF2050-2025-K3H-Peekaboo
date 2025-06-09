package com.pekaboo.features.navbar;

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
import com.pekaboo.features.home.MainController;

public class NavbarOptoController implements Initializable{
    private MainController mainController;
    private boolean isProfileActive = false;

    @FXML
    private Button homeButton;
    @FXML
    private Button jadwalButton;
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
            Image logo = new Image(getClass().getResourceAsStream("/com/pekaboo/navbar/assets/logo.png"));
            logoImageView.setImage(logo);

            updateProfileIcon();
            
        } catch (Exception e) {
            System.err.println("FATAL: Gagal memuat gambar logo atau profile. Pastikan file ada di resources/com/pekaboo/navbar/assets/");
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

        String fxmlPath = null;
        String cssPath = null;

        if (userData != null && !userData.isEmpty()) {
            try {
                fxmlPath = (String) userData.get(0);
                cssPath = (userData.size() > 1) ? (String) userData.get(1) : "null";

                System.out.println(">> fxmlPath = " + fxmlPath);
                System.out.println(">> cssPath = " + cssPath);

            } catch (Exception e) {
                System.err.println("⚠️ Gagal membaca fxmlPath atau cssPath dari userData.");
                e.printStackTrace();
            }

            try {
                if ("placeholder".equalsIgnoreCase(fxmlPath)) {
                    mainController.loadPage(null, null);
                } else {
                    if ("null".equalsIgnoreCase(cssPath)) {
                        cssPath = null;
                    }
                    mainController.loadPage(fxmlPath, cssPath);
                }

                setActiveButton(clickedButton);
            } catch (Exception e) {
                System.err.println("⚠️ Gagal memuat halaman: " + fxmlPath);
                e.printStackTrace();
            }
        } else {
            System.err.println("⚠️ userData kosong atau null untuk tombol: " + clickedButton.getText());
        }

        isProfileActive = false;
        updateProfileIcon();
    }

    @FXML
    void handleProfileClick(MouseEvent event) {
        System.out.println("Tombol Profile diklik!");
        
        isProfileActive = true;
        
        mainController.loadPage("/com/pekaboo/profile/Profile.fxml", null);
        
        setActiveButton(null);
        
        updateProfileIcon();
    }

    public void updateProfileIcon() {
        try {
            //User currentUser = Session.getCurrentUser();
            String profileIconPath;

            if (isProfileActive) {
                profileIconPath = "/com/pekaboo/navbar/assets/profile_icon_clicked.png";
            } else {
                profileIconPath = "/com/pekaboo/navbar/assets/ProfileIcon.png";
            }

            Image profileIcon = new Image(getClass().getResourceAsStream(profileIconPath));
            profileImageView.setImage(profileIcon);
            
        } catch (Exception e) {
            System.err.println("FATAL: Gagal memuat gambar ikon profil. Pastikan file ada di resources/com/pekaboo/navbar/assets/");
            e.printStackTrace();
        }
    }
    
    public void setActiveButton(Button activeButton) {
        homeButton.getStyleClass().remove("active");
        jadwalButton.getStyleClass().remove("active");
        historyButton.getStyleClass().remove("active");
        
        if (activeButton != null) {
            activeButton.getStyleClass().add("active");
        }
        
        // Reset profile state jika button lain diklik
        if (activeButton != null) {
            isProfileActive = false;
        }
    }
}
