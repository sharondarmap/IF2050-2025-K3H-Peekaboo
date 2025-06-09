package com.pekaboo.features.profile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import com.pekaboo.entities.Resep;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ResepRepository;
import com.pekaboo.repositories.postgres.PostgresResepRepository;
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

    private StackPane rootStack;
    private final ResepRepository resepRepo = new PostgresResepRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyStyles();

        loadEditIcon();

        populateUserData();
        loadPrescriptionData();
    }

    private void applyStyles() {
        if (profilePageContainer != null) {
            profilePageContainer.setStyle("-fx-background-color: #FAFAFA;");
            profilePageContainer.setPadding(new Insets(44, 72, 44, 72));
        }

        if (sectionHeader != null) {
            sectionHeader.setPadding(new Insets(0, 0, 32, 0));
        }

        if (sectionTitle != null) {
            sectionTitle.setStyle(
                "-fx-font-family: 'Nunito Sans';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 32px;" +
                "-fx-text-fill: black;"
            );
        }

        if (prescriptionTitle != null) {
            prescriptionTitle.setStyle(
                "-fx-font-family: 'Nunito Sans';" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 32px;" +
                "-fx-text-fill: black;"
            );
        }

        if (editButton != null) {
            editButton.setCursor(Cursor.HAND);
            
            // Hover effect untuk edit button
            editButton.setOnMouseEntered(e -> editButton.setOpacity(0.9));
            editButton.setOnMouseExited(e -> editButton.setOpacity(1.0));
        }

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

        if (prescriptionContainer != null) {
            prescriptionContainer.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 32px;"
                // "-fx-border-color: #E2E2E2;" +
                // "-fx-border-width: 2px;" +
                // "-fx-border-radius: 32px;"
            );
            //prescriptionContainer.setPadding(new Insets(44));
            prescriptionContainer.setSpacing(32);
        }

        if (prescriptionMessage != null) {
            prescriptionMessage.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 24px;" + 
                "-fx-text-fill: #939698;"
            );
        }

        if (infoGrid != null) {
            infoGrid.setHgap(160.0); // Increased horizontal gap for better spacing
            infoGrid.setVgap(48.0); // Increased vertical gap
        }

        applyInfoLabelStyle(nameLabelText);
        applyInfoLabelStyle(genderLabelText);
        applyInfoLabelStyle(emailLabelText);
        applyInfoLabelStyle(phoneLabelText);
        applyInfoLabelStyle(dobLabelText);
        applyInfoLabelStyle(addressLabelText);

        applyInfoDataStyle(nameLabel);
        applyInfoDataStyle(genderLabel);
        applyInfoDataStyle(emailLabel);
        applyInfoDataStyle(phoneLabel);
        applyInfoDataStyle(dobLabel);
        applyInfoDataStyle(addressLabel);

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
            
            label.setWrapText(true);
            label.setMaxWidth(524); // Lebar maksimum untuk label
            //label.setPrefWidth(-1);
        }
    }

    private void applyAddressStyle() {
        if (addressLabel != null) {
            addressLabel.setStyle(
                "-fx-font-family: 'Violet Sans';" +
                "-fx-font-size: 24px;" + 
                "-fx-text-fill: #364C84;"
            );
            addressLabel.setWrapText(true);
            addressLabel.setMaxWidth(524.0);
            addressLabel.setPrefWidth(524.0);
        }
    }

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

    // TODO : ini edit blm ya
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
        User currentUser = Session.getCurrentUser();
        
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

    private void loadPrescriptionData() {
        User currentUser = Session.getCurrentUser();
        
        if (currentUser == null) {
            prescriptionMessage.setText("Please log in to view prescriptions.");
            prescriptionMessage.setVisible(true);
            return;
        }

        try {
            List<Resep> prescriptions = resepRepo.getResepByPelanggan(currentUser);
            
            if (prescriptions.isEmpty()) {
                prescriptionMessage.setText("No prescriptions available.");
                prescriptionMessage.setVisible(true);
            } else {
                prescriptionMessage.setVisible(false);
                prescriptionContainer.getChildren().clear();
                
                for (Resep resep : prescriptions) {
                    VBox prescriptionCard = createPrescriptionCard(resep);
                    prescriptionContainer.getChildren().add(prescriptionCard);
                }
            }
            
        } catch (Exception e) {
            prescriptionMessage.setText("Error loading prescriptions: " + e.getMessage());
            prescriptionMessage.setVisible(true);
            e.printStackTrace();
        }
    }

   private VBox createPrescriptionCard(Resep resep) {
        VBox card = new VBox();
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 32px;" +
            "-fx-border-color: #E2E2E2;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 32px;"
        );
        card.setPadding(new Insets(44));
        card.setSpacing(0);

        GridPane prescriptionGrid = new GridPane();
        prescriptionGrid.setHgap(80.0);
        prescriptionGrid.setVgap(48.0);
        
        ColumnConstraints col1 = new ColumnConstraints(400);
        ColumnConstraints col2 = new ColumnConstraints(400);
        prescriptionGrid.getColumnConstraints().addAll(col1, col2);
        
        createFixedPrescriptionField(prescriptionGrid, "Examination Date", resep.getJadwal().getTanggal().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")), 0, 0);
        createFixedPrescriptionField(prescriptionGrid, "Issued by", (resep.getOptometris() != null ? resep.getOptometris().getUsername() : "Unknown"), 1, 0);
        
        String rightEyeData = String.format("Sphere: %s\nCyl: %s\nAxis: %s", 
            formatSphere(resep.getPlusKanan(), resep.getMinusKanan()),
            formatCylinder(resep.getCylKanan()),
            formatAxis(resep.getAxisKanan())
        );
        createFixedPrescriptionField(prescriptionGrid, "Right Eye", rightEyeData, 0, 1);
        
        String leftEyeData = String.format("Sphere: %s\nCyl: %s\nAxis: %s", 
            formatSphere(resep.getPlusKiri(), resep.getMinusKiri()),
            formatCylinder(resep.getCylKiri()),
            formatAxis(resep.getAxisKiri())
        );
        createFixedPrescriptionField(prescriptionGrid, "Left Eye", leftEyeData, 1, 1);
        
        createFixedPrescriptionField(prescriptionGrid, "PD (Pupillary Distance)", resep.getPd() + " mm", 0, 2);
        
        card.getChildren().add(prescriptionGrid);
        return card;
    }

    private void createFixedPrescriptionField(GridPane grid, String labelText, String valueText, int col, int row) {
        VBox fieldBox = new VBox();
        fieldBox.setSpacing(8);
        
        Label label = new Label(labelText);
        label.setStyle(
            "-fx-font-family: 'Violet Sans';" +
            "-fx-font-size: 20px;" + 
            "-fx-text-fill: #939698;"
        );
        
        Label value = new Label(valueText);
        value.setStyle(
            "-fx-font-family: 'Violet Sans';" +
            "-fx-font-size: 24px;" + 
            "-fx-text-fill: #364C84;"
        );
        
        value.setWrapText(true);
        value.setPrefWidth(380);
        value.setMaxWidth(380);
        value.setMinHeight(Region.USE_PREF_SIZE);
        
        fieldBox.getChildren().addAll(label, value);
        grid.add(fieldBox, col, row);
    }

    private String formatSphere(double plus, double minus) {
        if (plus == 0 && minus == 0) {
            return "0.00";
        } else if (plus != 0 && minus == 0) {
            return String.format("+%.2f", plus);
        } else if (plus == 0 && minus != 0) {
            return String.format("%.2f", minus);
        } else {
            return String.format("+%.2f/%.2f", plus, minus);
        }
    }

    private String formatCylinder(double cyl) {
        if (cyl == 0) {
            return "0.00";
        }
        return String.format("%+.2f", cyl);
    }

    private String formatAxis(double axis) {
        if (axis == 0) {
            return "0°";
        }
        return String.format("%.0f°", axis);
    }


    private void openEditOverlay() {
        if (rootStack == null) {
            System.err.println("❌ rootStack is null. Harus di-set dari MainController!");
            return;
        }

        User currentUser = Session.getCurrentUser();

        VBox overlay = new VBox(8);
        overlay.setPadding(new Insets(40));
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 8);"
        );
        overlay.setMaxWidth(400);
        overlay.setMaxHeight(500);

        Label title = new Label("Edit Profile");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPromptText("Email");
        emailField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: rgba(36, 22, 80, 1); " +
            "-fx-padding: 10 12; " + 
            "-fx-min-height: 36px; " + 
            "-fx-background-color: #F8FAFC; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6;"
        );

        TextField phoneField = new TextField(currentUser.getNoTelepon());
        phoneField.setPromptText("Phone Number");
        phoneField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: rgba(36, 22, 80, 1); " +
            "-fx-padding: 10 12; " + 
            "-fx-min-height: 36px; " + 
            "-fx-background-color: #F8FAFC; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6;"
        );

        TextField alamatField = new TextField(currentUser.getAlamat());
        alamatField.setPromptText("Alamat");
        alamatField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: rgba(36, 22, 80, 1); " +
            "-fx-padding: 10 12; " + 
            "-fx-min-height: 36px; " + 
            "-fx-background-color: #F8FAFC; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6;"
        );

        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Laki-laki", "Perempuan", "Lainnya");
        genderCombo.setValue(currentUser.getJenisKelamin());

        genderCombo.setStyle(  
            "-fx-background-color: rgb(255, 255, 255);" +
            "-fx-mark-color: #5a29e4;");

        Button saveBtn = new Button("Simpan");
        Button cancelBtn = new Button("Batal");

        saveBtn.setStyle(
            "-fx-background-color: rgba(91, 54, 201, 1); " +
            "-fx-text-fill: rgba(255, 255, 255, 1); " +
            "-fx-padding: 8 24; " +
            "-fx-background-radius: 10; " +
            "-fx-font-weight: 700; " +
             "-fx-font-size: 14px; ");

        cancelBtn.setStyle(
            "-fx-background-color: #9CA3AF; " +
            "-fx-text-fill: rgb(0, 0, 0); " +
            "-fx-padding: 8 24; " +
            "-fx-background-radius: 10; " +
            "-fx-font-weight: 700; " +
             "-fx-font-size: 14px; ");

        HBox buttonBox = new HBox(10, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        overlay.getChildren().addAll(title, emailField, phoneField, alamatField, genderCombo, buttonBox);

        StackPane overlayWrapper = new StackPane(overlay);
        overlayWrapper.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        overlayWrapper.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlayWrapper.setAlignment(Pos.CENTER);

        rootStack.getChildren().add(overlayWrapper);

        cancelBtn.setOnAction(e -> rootStack.getChildren().remove(overlayWrapper));

        saveBtn.setOnAction(e -> {
            currentUser.setEmail(emailField.getText());
            currentUser.setNoTelepon(phoneField.getText());
            currentUser.setAlamat(alamatField.getText());
            currentUser.setJenisKelamin(genderCombo.getValue());

            new com.pekaboo.repositories.postgres.PostgresUserRepository().updateUser(currentUser);

            refreshUserData();
            rootStack.getChildren().remove(overlayWrapper);
        });
    }

    @FXML
    private void handleEditProfile(MouseEvent event) {
        System.out.println("Edit button clicked!");
        openEditOverlay();
        // Implementasi untuk edit profile bisa ditambahkan di sini
        // Misalnya membuka dialog edit atau navigasi ke halaman edit
    }

    // Method untuk refresh data jika diperlukan
    public void refreshUserData() {
        populateUserData();
        loadPrescriptionData();
    }

    public void setRootStack(StackPane rootStack) {
        this.rootStack = rootStack;
    }

}