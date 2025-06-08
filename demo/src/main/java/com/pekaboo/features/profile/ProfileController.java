package com.pekaboo.features.profile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.pekaboo.entities.User;
import com.pekaboo.util.Session;

public class ProfileController implements Initializable {

    @FXML
    private VBox profilePageContainer;
    @FXML
    private HBox sectionHeader;
    @FXML
    private Label sectionTitle;
    @FXML
    private ImageView editButton;
    @FXML
    private VBox infoContainer;
    @FXML
    private GridPane infoGrid;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label nameLabelText;
    @FXML
    private Label genderLabelText;
    @FXML
    private Label emailLabelText;
    @FXML
    private Label phoneLabelText;
    @FXML
    private Label dobLabelText;
    @FXML
    private Label addressLabelText;
    
    // Prescription section elements
    @FXML
    private Label prescriptionTitle;
    @FXML
    private VBox prescriptionContainer;
    @FXML
    private Label prescriptionMessage;

    // Data dummy untuk demonstrasi
    private User dummyUser;
    
    // Reference ke MainController jika diperlukan
    private Object mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Terapkan styling
        applyStyles();
        
        // Membuat data dummy user
        createDummyUser();
        
        // Load edit icon
        loadEditIcon();
        
        // Populate data
        populateUserData();
    }

    private void applyStyles() {
        // Style untuk container utama halaman profil
        if (profilePageContainer != null) {
            profilePageContainer.setStyle("-fx-background-color: #FAFAFA;");
            profilePageContainer.setPadding(new Insets(44, 72, 44, 72));
        }

        // Style untuk header section
        if (sectionHeader != null) {
            sectionHeader.setPadding(new Insets(0, 0, 32, 0));
        }

        // Style untuk judul section
        if (sectionTitle != null) {
            sectionTitle.setStyle(
                "-fx-font-family: 'Nunito Sans';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 32px;" +
                "-fx-text-fill: black;"
            );
        }

        // Style untuk prescription title
        if (prescriptionTitle != null) {
            prescriptionTitle.setStyle(
                "-fx-font-family: 'Nunito Sans';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 32px;" +
                "-fx-text-fill: black;"
            );
        }

        // Style untuk tombol edit (ImageView)
        if (editButton != null) {
            editButton.setCursor(Cursor.HAND);
            
            // Hover effect untuk edit button
            editButton.setOnMouseEntered(e -> editButton.setOpacity(0.9));
            editButton.setOnMouseExited(e -> editButton.setOpacity(1.0));
        }

        // Style untuk kotak informasi utama
        if (infoContainer != null) {
            infoContainer.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 32px;" +
                "-fx-border-color: #E2E2E2;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 32px;"
            );
            infoContainer.setPadding(new Insets(44));
        }

        // Style untuk prescription container
        if (prescriptionContainer != null) {
            prescriptionContainer.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 32px;" +
                "-fx-border-color: #E2E2E2;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 32px;"
            );
            prescriptionContainer.setPadding(new Insets(44));
        }

        // Style untuk prescription message
        if (prescriptionMessage != null) {
            prescriptionMessage.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 24px;" + 
                "-fx-text-fill: #939698;"
            );
        }

        // Style untuk GridPane
        if (infoGrid != null) {
            infoGrid.setHgap(160.0); // Increased horizontal gap for better spacing
            infoGrid.setVgap(48.0); // Increased vertical gap
        }

        // Style untuk label informasi (kiri - nama field)
        applyInfoLabelStyle(nameLabelText);
        applyInfoLabelStyle(genderLabelText);
        applyInfoLabelStyle(emailLabelText);
        applyInfoLabelStyle(phoneLabelText);
        applyInfoLabelStyle(dobLabelText);
        applyInfoLabelStyle(addressLabelText);

        // Style untuk data pengguna (kanan - nilai field)
        applyInfoDataStyle(nameLabel);
        applyInfoDataStyle(genderLabel);
        applyInfoDataStyle(emailLabel);
        applyInfoDataStyle(phoneLabel);
        applyInfoDataStyle(dobLabel);
        applyInfoDataStyle(addressLabel);

        // Style khusus untuk alamat yang panjang
        applyAddressStyle();
    }

    private void applyInfoLabelStyle(Label label) {
        if (label != null) {
            label.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 20px;" + 
                "-fx-text-fill: #939698;"
            );
        }
    }

    private void applyInfoDataStyle(Label label) {
        if (label != null) {
            label.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 24px;" + 
                "-fx-text-fill: #364C84;"
            );
            
            // Default setting untuk semua label (nama, email, dll)
            label.setWrapText(true);
            label.setMaxWidth(524); // Lebar maksimum untuk label
            //label.setPrefWidth(-1);
        }
    }

    // Method khusus untuk styling alamat yang panjang
    private void applyAddressStyle() {
        if (addressLabel != null) {
            addressLabel.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 24px;" + 
                "-fx-text-fill: #364C84;"
            );
            // Khusus alamat: enable wrapping dan batasi width
            addressLabel.setWrapText(true);
            addressLabel.setMaxWidth(524.0);
            addressLabel.setPrefWidth(524.0);
        }
    }

    // Method untuk mengubah style secara individual
    public void setProfilePageBackgroundColor(String color) {
        if (profilePageContainer != null) {
            profilePageContainer.setStyle("-fx-background-color: " + color + ";");
        }
    }

    public void setSectionTitleColor(String color) {
        if (sectionTitle != null) {
            String currentStyle = sectionTitle.getStyle();
            String newStyle = currentStyle.replaceAll("-fx-text-fill: [^;]+;", "") + "-fx-text-fill: " + color + ";";
            sectionTitle.setStyle(newStyle);
        }
    }

    public void setEditButtonOpacity(double opacity) {
        if (editButton != null) {
            editButton.setOpacity(opacity);
        }
    }

    public void setInfoContainerBackgroundColor(String color) {
        if (infoContainer != null) {
            String currentStyle = infoContainer.getStyle();
            String newStyle = currentStyle.replaceAll("-fx-background-color: [^;]+;", "") + "-fx-background-color: " + color + ";";
            infoContainer.setStyle(newStyle);
        }
    }

    public void setInfoLabelColor(String color) {
        String newStyle = "-fx-font-family: 'Violet Sans'; -fx-font-size: 16px; -fx-text-fill: " + color + ";";
        if (nameLabelText != null) nameLabelText.setStyle(newStyle);
        if (genderLabelText != null) genderLabelText.setStyle(newStyle);
        if (emailLabelText != null) emailLabelText.setStyle(newStyle);
        if (phoneLabelText != null) phoneLabelText.setStyle(newStyle);
        if (dobLabelText != null) dobLabelText.setStyle(newStyle);
        if (addressLabelText != null) addressLabelText.setStyle(newStyle);
    }

    public void setInfoDataColor(String color) {
        String newStyle = "-fx-font-family: 'Violet Sans'; -fx-font-size: 20px; -fx-text-fill: " + color + ";";
        if (nameLabel != null) nameLabel.setStyle(newStyle);
        if (genderLabel != null) genderLabel.setStyle(newStyle);
        if (emailLabel != null) emailLabel.setStyle(newStyle);
        if (phoneLabel != null) phoneLabel.setStyle(newStyle);
        if (dobLabel != null) dobLabel.setStyle(newStyle);
        if (addressLabel != null) addressLabel.setStyle(newStyle);
    }

    private void createDummyUser() {
        dummyUser = new User();
        dummyUser.setIdUser(1);
        dummyUser.setUsername("Nathania Ammara Salsabilla");
        dummyUser.setEmail("scenesbynath@gmail.com");
        dummyUser.setAlamat("Jl. Garuda No. 25, RT 01/RW 05, Kemayoran, Jakarta Pusat 10620");
        dummyUser.setTanggalLahir(LocalDate.of(2004, 1, 25));
        dummyUser.setJenisKelamin("Female");
        dummyUser.setNoTelepon("(+62) 821-2504-4378");
        dummyUser.setUserStatus("Customer");
        dummyUser.setPassword("password123");
    }

    private void loadEditIcon() {
        try {
            Image editImage = new Image(getClass().getResourceAsStream("/com/pekaboo/profile/assets/icon_edit.png"));
            editButton.setImage(editImage);
            editButton.setFitHeight(44.0);
            editButton.setFitWidth(44.0);
            editButton.setPreserveRatio(true);
            editButton.setPickOnBounds(true);
        } catch (Exception e) {
            System.err.println("Warning: Gagal memuat icon edit. Pastikan file ada di resources/com/pekaboo/profile/assets/icon_edit.png");
            e.printStackTrace();
        }
    }

    private void populateUserData() {
        // Check if we have a current user in session, otherwise use dummy data
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            currentUser = dummyUser;
        }

        // Populate labels with user data
        nameLabel.setText(currentUser.getUsername() != null ? currentUser.getUsername() : "N/A");
        emailLabel.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "N/A");
        addressLabel.setText(currentUser.getAlamat() != null ? currentUser.getAlamat() : "N/A");
        genderLabel.setText(currentUser.getJenisKelamin() != null ? currentUser.getJenisKelamin() : "N/A");
        phoneLabel.setText(currentUser.getNoTelepon() != null ? currentUser.getNoTelepon() : "N/A");
        
        // Format date of birth
        if (currentUser.getTanggalLahir() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            dobLabel.setText(currentUser.getTanggalLahir().format(formatter));
        } else {
            dobLabel.setText("N/A");
        }
    }

    @FXML
    private void handleEditProfile(MouseEvent event) {
        System.out.println("Edit button clicked!");
        // Implementasi untuk edit profile bisa ditambahkan di sini
        // Misalnya membuka dialog edit atau navigasi ke halaman edit
    }

    // Method untuk refresh data jika diperlukan
    public void refreshUserData() {
        populateUserData();
    }

    // Method untuk set MainController reference (diperlukan oleh MainController)
    public void setMainController(Object mainController) {
        this.mainController = mainController;
        System.out.println("MainController reference set to ProfileController");
    }
    
    // Method untuk mendapatkan MainController reference jika diperlukan
    public Object getMainController() {
        return this.mainController;
    }
}