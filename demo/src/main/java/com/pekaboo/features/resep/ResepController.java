package com.pekaboo.features.resep;

import com.pekaboo.entities.Resep;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ResepRepository;
import com.pekaboo.repositories.postgres.PostgresResepRepository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ResepController {
    private final ResepRepository resepRepo = new PostgresResepRepository();

    public ResepController() {
    }

    /**
     * @param reservasi
     * @param rootStack
     * @param currentOptometris
     */
    public void showAddPrescriptionOverlay(Reservasi reservasi, StackPane rootStack, User currentOptometris) {
        ScrollPane scrollPane = new ScrollPane();
        VBox overlay = createPrescriptionForm(reservasi, rootStack, currentOptometris);
        
        scrollPane.setContent(overlay);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox dimmer = new VBox(scrollPane);
        dimmer.setAlignment(Pos.CENTER);
        dimmer.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        dimmer.setPrefSize(rootStack.getWidth(), rootStack.getHeight());

        rootStack.getChildren().add(dimmer);
    }

    private VBox createPrescriptionForm(Reservasi reservasi, StackPane rootStack, User currentOptometris) {
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.TOP_CENTER);
        overlay.setPadding(new Insets(30));
        overlay.setMaxWidth(450);
        overlay.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 8);"
        );

        // 🔧 Header with blue background
        VBox headerSection = new VBox(5);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setPadding(new Insets(15, 20, 15, 20));
        headerSection.setStyle(
            "-fx-background-color: #5B36C9; " +
            "-fx-background-radius: 8; " +
            "-fx-border-radius: 8;"
        );

        Label title = new Label("Add Prescription");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: white;");
        
        Label customerInfo = new Label("Customer: " + reservasi.getPelanggan().getUsername());
        customerInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255, 255, 255, 0.9);");
        
        headerSection.getChildren().addAll(title, customerInfo);

        // 🔧 Sphere Section (SPH)
        Label sphereTitle = new Label("Sphere");
        sphereTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #374151;");

        GridPane sphereGrid = new GridPane();
        sphereGrid.setHgap(20);
        sphereGrid.setVgap(15);
        sphereGrid.setAlignment(Pos.CENTER);

        // Right Eye SPH
        Label rightSphLabel = new Label("Right");
        rightSphLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        VBox rightSphBox = new VBox(8);
        rightSphBox.setAlignment(Pos.CENTER);
        
        HBox rightSphInputs = new HBox(10);
        rightSphInputs.setAlignment(Pos.CENTER);
        
        TextField rightSphPlus = new TextField("0");
        rightSphPlus.setPrefWidth(60);
        styleTextField(rightSphPlus);
        
        Label rightSphMinus = new Label("-");
        rightSphMinus.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        
        TextField rightSphMinusField = new TextField("0");
        rightSphMinusField.setPrefWidth(60);
        styleTextField(rightSphMinusField);
        
        rightSphInputs.getChildren().addAll(rightSphPlus, rightSphMinus, rightSphMinusField);
        rightSphBox.getChildren().addAll(rightSphLabel, rightSphInputs);

        // Left Eye SPH
        Label leftSphLabel = new Label("Left");
        leftSphLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        VBox leftSphBox = new VBox(8);
        leftSphBox.setAlignment(Pos.CENTER);
        
        HBox leftSphInputs = new HBox(10);
        leftSphInputs.setAlignment(Pos.CENTER);
        
        TextField leftSphPlus = new TextField("0");
        leftSphPlus.setPrefWidth(60);
        styleTextField(leftSphPlus);
        
        Label leftSphMinus = new Label("-");
        leftSphMinus.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        
        TextField leftSphMinusField = new TextField("0");
        leftSphMinusField.setPrefWidth(60);
        styleTextField(leftSphMinusField);
        
        leftSphInputs.getChildren().addAll(leftSphPlus, leftSphMinus, leftSphMinusField);
        leftSphBox.getChildren().addAll(leftSphLabel, leftSphInputs);

        sphereGrid.add(rightSphBox, 0, 0);
        sphereGrid.add(leftSphBox, 1, 0);

        // 🔧 Cylinder Section (CYL)
        Label cylinderTitle = new Label("Cylinder (Cyl)");
        cylinderTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #374151;");

        GridPane cylinderGrid = new GridPane();
        cylinderGrid.setHgap(20);
        cylinderGrid.setVgap(15);
        cylinderGrid.setAlignment(Pos.CENTER);

        // Right Eye CYL
        VBox rightCylBox = new VBox(8);
        rightCylBox.setAlignment(Pos.CENTER);
        
        Label rightCylLabel = new Label("Right");
        rightCylLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        TextField rightCylField = new TextField("0");
        rightCylField.setPrefWidth(80);
        styleTextField(rightCylField);
        
        rightCylBox.getChildren().addAll(rightCylLabel, rightCylField);

        // Left Eye CYL
        VBox leftCylBox = new VBox(8);
        leftCylBox.setAlignment(Pos.CENTER);
        
        Label leftCylLabel = new Label("Left");
        leftCylLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        TextField leftCylField = new TextField("0");
        leftCylField.setPrefWidth(80);
        styleTextField(leftCylField);
        
        leftCylBox.getChildren().addAll(leftCylLabel, leftCylField);

        cylinderGrid.add(rightCylBox, 0, 0);
        cylinderGrid.add(leftCylBox, 1, 0);

        // 🔧 Axis Section
        Label axisTitle = new Label("Axis");
        axisTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #374151;");

        GridPane axisGrid = new GridPane();
        axisGrid.setHgap(20);
        axisGrid.setVgap(15);
        axisGrid.setAlignment(Pos.CENTER);

        // Right Eye Axis
        VBox rightAxisBox = new VBox(8);
        rightAxisBox.setAlignment(Pos.CENTER);
        
        Label rightAxisLabel = new Label("Right");
        rightAxisLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        TextField rightAxisField = new TextField("0");
        rightAxisField.setPrefWidth(80);
        styleTextField(rightAxisField);
        
        rightAxisBox.getChildren().addAll(rightAxisLabel, rightAxisField);

        // Left Eye Axis
        VBox leftAxisBox = new VBox(8);
        leftAxisBox.setAlignment(Pos.CENTER);
        
        Label leftAxisLabel = new Label("Left");
        leftAxisLabel.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        TextField leftAxisField = new TextField("0");
        leftAxisField.setPrefWidth(80);
        styleTextField(leftAxisField);
        
        leftAxisBox.getChildren().addAll(leftAxisLabel, leftAxisField);

        axisGrid.add(rightAxisBox, 0, 0);
        axisGrid.add(leftAxisBox, 1, 0);

        // 🔧 PD Section
        Label pdTitle = new Label("PD (Pupillary Distance)");
        pdTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #374151;");

        TextField pdField = new TextField("63");
        pdField.setPrefWidth(100);
        pdField.setAlignment(Pos.CENTER);
        styleTextField(pdField);

        VBox pdSection = new VBox(10);
        pdSection.setAlignment(Pos.CENTER);
        pdSection.getChildren().addAll(pdTitle, pdField);

        // 🔧 Action Buttons
        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(10, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(120);
        cancelBtn.setStyle(
            "-fx-background-color: #E5E7EB; " +
            "-fx-text-fill: #374151; " +
            "-fx-padding: 12 24; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 600; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand;"
        );

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(120);
        saveBtn.setStyle(
            "-fx-background-color: #5B36C9; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 12 24; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 600; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand;"
        );

        actionBox.getChildren().addAll(cancelBtn, saveBtn);

        // 🔧 Add all sections to overlay
        overlay.getChildren().addAll(
            headerSection,
            sphereTitle, sphereGrid,
            cylinderTitle, cylinderGrid,
            axisTitle, axisGrid,
            pdSection,
            actionBox
        );

        // 🔧 Button Actions
        cancelBtn.setOnAction(e -> removePrescriptionOverlay(rootStack));

        saveBtn.setOnAction(e -> {
            try {
                // 🔧 Extract and save prescription data
                Resep resep = new Resep();
                
                // Parse sphere values
                resep.setPlusKanan(Double.parseDouble(rightSphPlus.getText()));
                resep.setMinusKanan(Double.parseDouble(rightSphMinusField.getText()));
                resep.setPlusKiri(Double.parseDouble(leftSphPlus.getText()));
                resep.setMinusKiri(Double.parseDouble(leftSphMinusField.getText()));
                
                // Parse cylinder values
                resep.setCylKanan(Double.parseDouble(rightCylField.getText()));
                resep.setCylKiri(Double.parseDouble(leftCylField.getText()));
                
                // Parse axis values
                resep.setAxisKanan(Double.parseDouble(rightAxisField.getText()));
                resep.setAxisKiri(Double.parseDouble(leftAxisField.getText()));
                
                // Parse PD
                resep.setPd(Double.parseDouble(pdField.getText()));
                
                // Set relationships
                resep.setPelanggan(reservasi.getPelanggan());
                resep.setOptometris(currentOptometris);
                resep.setJadwal(reservasi.getJadwal());

                // Save to database
                resepRepo.addResep(resep);

                removePrescriptionOverlay(rootStack);
                showSuccessAlert(reservasi.getPelanggan().getUsername());

            } catch (NumberFormatException ex) {
                showErrorAlert("Please enter valid numbers for all fields");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Failed to save prescription: " + ex.getMessage());
            }
        });

        return overlay;
    }

    /**
     * Styles a TextField with consistent design
     */
    private void styleTextField(TextField textField) {
        textField.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #D1D5DB; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-padding: 8 12; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #374151; " +
            "-fx-alignment: center;"
        );
        
        // Focus effect
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #5B36C9; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 6; " +
                    "-fx-background-radius: 6; " +
                    "-fx-padding: 7 11; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-alignment: center;"
                );
            } else {
                textField.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #D1D5DB; " +
                    "-fx-border-width: 1; " +
                    "-fx-border-radius: 6; " +
                    "-fx-background-radius: 6; " +
                    "-fx-padding: 8 12; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-alignment: center;"
                );
            }
        });
    }

    private void removePrescriptionOverlay(StackPane rootStack) {
        rootStack.getChildren().removeIf(node -> {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                return vbox.getStyle().contains("rgba(0,0,0,0.6)");
            }
            return false;
        });
    }

    private void showSuccessAlert(String customerName) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("✅ Berhasil");
        successAlert.setHeaderText("Resep Berhasil Disimpan");
        successAlert.setContentText("Resep untuk " + customerName + " telah berhasil ditambahkan.");
        successAlert.showAndWait();
    }

    private void showErrorAlert(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("❌ Error");
        errorAlert.setHeaderText("Gagal Menyimpan Resep");
        errorAlert.setContentText("Terjadi kesalahan: " + errorMessage);
        errorAlert.showAndWait();
    }

    public java.util.List<Resep> getAllResepByPelanggan(User pelanggan) {
        return resepRepo.getResepByPelanggan(pelanggan);
    }

    public Resep getResepById(int idResep) {
        return resepRepo.getResepById(idResep);
    }

    public void updatePrescription(Resep resep) {
        try {
            resepRepo.updateResep(resep);
            showSuccessAlert("Prescription updated successfully");
        } catch (Exception e) {
            showErrorAlert("Failed to update prescription: " + e.getMessage());
        }
    }

    public void deletePrescription(int resepId) {
        try {
            resepRepo.deleteResep(resepId);
            showSuccessAlert("Prescription deleted successfully");
        } catch (Exception e) {
            showErrorAlert("Failed to delete prescription: " + e.getMessage());
        }
    }

    public void showCustomerPrescriptionHistory(User customer, StackPane rootStack) {
        // TODO: Implement prescription history view
        // This could show a table of all prescriptions for the customer
        System.out.println("Showing prescription history for: " + customer.getUsername());
    }
}