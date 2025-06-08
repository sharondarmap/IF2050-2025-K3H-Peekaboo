package com.pekaboo.features.resep;

import com.pekaboo.entities.Resep;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ResepRepository;
import com.pekaboo.repositories.postgres.PostgresResepRepository;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.List;

public class ResepController {
    private final ResepRepository resepRepo = new PostgresResepRepository();
    
    private TextField rightPlusField, leftPlusField, rightMinusField, leftMinusField;
    private TextField rightCylField, leftCylField;
    private TextField rightAxisField, leftAxisField;
    private TextField pdField;

    private boolean isUpdateMode = false;
    private Resep existingResep = null;

    public ResepController() {
    }

    /**
     * @param reservasi
     * @param rootStack
     * @param currentOptometris
     */

    public void showAddPrescriptionOverlay(Reservasi reservasi, StackPane rootStack, User currentOptometris) {
        showPrescriptionOverlay(reservasi, rootStack, currentOptometris, null);
    }

    // ini kalo udah pernah ngisi & mau update resepnya
    public void showUpdatePrescriptionOverlay(Reservasi reservasi, StackPane rootStack, User currentOptometris, Resep existingResep) {
        showPrescriptionOverlay(reservasi, rootStack, currentOptometris, existingResep);
    }

    public void showPrescriptionOverlay(Reservasi reservasi, StackPane rootStack, User currentOptometris, Resep existingResep) {
        this.isUpdateMode = (existingResep != null);
        this.existingResep = existingResep;
        
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
        overlay.setAlignment(Pos.CENTER);
        overlay.setPrefWidth(rootStack.getWidth());
        overlay.setPrefHeight(rootStack.getHeight());

        VBox popup = new VBox(8);
        popup.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 32px 40px;" +
            "-fx-background-radius: 28;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 24, 0, 0, 12);"
        );
        popup.setMaxWidth(600); 
        popup.setMinWidth(400);

        Label title = new Label(isUpdateMode ? "Update Prescription" : "Add Prescription");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: rgba(91, 54, 201, 1); -fx-alignment: center;");

        Label customerInfo = new Label("Patient: " + reservasi.getPelanggan().getUsername());
        customerInfo.setMaxWidth(Double.MAX_VALUE);
        customerInfo.setAlignment(Pos.CENTER);
        customerInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(36, 22, 80, 1); -fx-padding: 0; -fx-alignment: center;");

        VBox sphereSection = createPrescriptionSection("Sphere (±)", "rgba(36, 22, 80, 1)", 160);
        VBox cylinderSection = createPrescriptionSection("Cylinder (Cyl)", "rgba(36, 22, 80, 1)", 160);
        VBox axisSection = createPrescriptionSection("Axis", "rgba(36, 22, 80, 1)", 160);
        VBox pdSection = createSingleFieldSection("PD (Pupillary Distance)", 340);

        Label errorLabel = new Label();
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 13px; -fx-padding: 4 0 0 0;");
        errorLabel.setVisible(false);

        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER); 
        buttonBox.setFillHeight(true);

        Button cancelBtn = new Button("Cancel");
        Button saveBtn = new Button(isUpdateMode ? "Update" : "Save");

        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.setPrefHeight(36);
        saveBtn.setPrefHeight(36);

        // biar buttonnya expand sesuai width
        HBox.setHgrow(cancelBtn, Priority.ALWAYS);
        HBox.setHgrow(saveBtn, Priority.ALWAYS);

        cancelBtn.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 1); " + 
            "-fx-text-fill: rgba(91, 54, 201, 1); " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 700; " +
            "-fx-font-size: 14px; " +
            "-fx-border-color: rgba(147, 150, 152, 1); " + 
            "-fx-border-width: 1; " +
            "-fx-border-radius: 8;"
        );
        saveBtn.setStyle(
            "-fx-background-color: rgba(91, 54, 201, 1); " +
            "-fx-text-fill: rgba(255, 255, 255, 1); " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 700; " +
            "-fx-font-size: 14px; "
        );

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);

        VBox errorAndButtons = new VBox(2);
        errorAndButtons.setFillWidth(true);
        errorAndButtons.getChildren().addAll(errorLabel, buttonBox);

        popup.getChildren().addAll(
            title,
            customerInfo,
            sphereSection,
            cylinderSection,
            axisSection,
            pdSection,
            errorAndButtons
        );

        overlay.getChildren().add(popup);
        rootStack.getChildren().add(overlay);
        overlay.toFront();

        cancelBtn.setOnAction(e -> rootStack.getChildren().remove(overlay));
        
        overlay.setOnMouseClicked(ev -> {
            if (ev.getTarget() == overlay) rootStack.getChildren().remove(overlay);
        });

        saveBtn.setOnAction(ev -> {
            List<TextField> fields = new ArrayList<>();
            collectTextFields(sphereSection, fields);
            collectTextFields(cylinderSection, fields);
            collectTextFields(axisSection, fields);
            collectTextFields(pdSection, fields);

            boolean allNumeric = true;
            for (TextField tf : fields) {
                String text = tf.getText();
                if (text == null || text.trim().isEmpty() || !text.matches("-?\\d+(\\.\\d+)?")) {
                    allNumeric = false;
                    break;
                }
            }
            if (!allNumeric) {
                errorLabel.setText("All prescription inputs must be numbers.");
                errorLabel.setVisible(true);
                return;
            }
            errorLabel.setVisible(false);

            // Save to database
            try {
                if (isUpdateMode) {
                    updatePrescriptionInDatabase(reservasi, currentOptometris);
                    rootStack.getChildren().remove(overlay);
                    showSuccessAlert(reservasi.getPelanggan().getUsername(), "updated");
                } else {
                    savePrescriptionToDatabase(reservasi, currentOptometris);
                    rootStack.getChildren().remove(overlay);
                    showSuccessAlert(reservasi.getPelanggan().getUsername(), "saved");
                }
            } catch (Exception ex) {
                errorLabel.setText("Failed to " + (isUpdateMode ? "update" : "save") + ": " + ex.getMessage());
                errorLabel.setVisible(true);
                ex.printStackTrace();
            }
        });
    }

    private VBox createPrescriptionSection(String label, String color, double inputWidth) {
        VBox section = new VBox(4);
        Label sectionLabel = new Label(label);
        sectionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER);
        row.setFillHeight(true);

        if (label.toLowerCase().contains("sphere")) {
            double boxWidth = (inputWidth * 2 + 28) / 4.0;

            VBox rightPlusBox = new VBox(2);
            Label rightPlusLabel = new Label("Right +");
            rightPlusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String rightPlusValue = isUpdateMode ? String.valueOf(existingResep.getPlusKanan()) : "0";
            rightPlusField = new TextField(rightPlusValue);
            rightPlusField.setPrefWidth(boxWidth);
            rightPlusField.setPrefHeight(28);
            styleTextField(rightPlusField);
            rightPlusBox.setFillWidth(true);
            rightPlusBox.getChildren().addAll(rightPlusLabel, rightPlusField);

            VBox leftPlusBox = new VBox(2);
            Label leftPlusLabel = new Label("Left +");
            leftPlusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String leftPlusValue = isUpdateMode ? String.valueOf(existingResep.getPlusKiri()) : "0";
            leftPlusField = new TextField(leftPlusValue);
            leftPlusField.setPrefWidth(boxWidth);
            leftPlusField.setPrefHeight(28);
            styleTextField(leftPlusField);
            leftPlusBox.setFillWidth(true);
            leftPlusBox.getChildren().addAll(leftPlusLabel, leftPlusField);

            VBox rightMinusBox = new VBox(2);
            Label rightMinusLabel = new Label("Right -");
            rightMinusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String rightMinusValue = isUpdateMode ? String.valueOf(existingResep.getMinusKanan()) : "0";
            rightMinusField = new TextField(rightMinusValue);
            rightMinusField.setPrefWidth(boxWidth);
            rightMinusField.setPrefHeight(28);
            styleTextField(rightMinusField);
            rightMinusBox.setFillWidth(true);
            rightMinusBox.getChildren().addAll(rightMinusLabel, rightMinusField);

            VBox leftMinusBox = new VBox(2);
            Label leftMinusLabel = new Label("Left -");
            leftMinusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String leftMinusValue = isUpdateMode ? String.valueOf(existingResep.getMinusKiri()) : "0";
            leftMinusField = new TextField(leftMinusValue);
            leftMinusField.setPrefWidth(boxWidth);
            leftMinusField.setPrefHeight(28);
            styleTextField(leftMinusField);
            leftMinusBox.setFillWidth(true);
            leftMinusBox.getChildren().addAll(leftMinusLabel, leftMinusField);

            HBox.setHgrow(rightPlusBox, Priority.ALWAYS);
            HBox.setHgrow(leftPlusBox, Priority.ALWAYS);
            HBox.setHgrow(rightMinusBox, Priority.ALWAYS);
            HBox.setHgrow(leftMinusBox, Priority.ALWAYS);

            row.getChildren().addAll(rightPlusBox, leftPlusBox, rightMinusBox, leftMinusBox);
            
        } else if (label.toLowerCase().contains("cylinder")) {
            VBox rightBox = new VBox(2);
            Label rightLabel = new Label("Right");
            rightLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String rightCylValue = isUpdateMode ? String.valueOf(existingResep.getCylKanan()) : "0";
            rightCylField = new TextField(rightCylValue);
            rightCylField.setPrefWidth(inputWidth);
            rightCylField.setPrefHeight(28);
            styleTextField(rightCylField);
            rightBox.setFillWidth(true);
            rightBox.getChildren().addAll(rightLabel, rightCylField);

            VBox leftBox = new VBox(2);
            Label leftLabel = new Label("Left");
            leftLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String leftCylValue = isUpdateMode ? String.valueOf(existingResep.getCylKiri()) : "0";
            leftCylField = new TextField(leftCylValue);
            leftCylField.setPrefWidth(inputWidth);
            leftCylField.setPrefHeight(28);
            styleTextField(leftCylField);
            leftBox.setFillWidth(true);
            leftBox.getChildren().addAll(leftLabel, leftCylField);

            HBox.setHgrow(rightBox, Priority.ALWAYS);
            HBox.setHgrow(leftBox, Priority.ALWAYS);

            row.getChildren().addAll(rightBox, leftBox);
            
        } else if (label.toLowerCase().contains("axis")) {
            VBox rightBox = new VBox(2);
            Label rightLabel = new Label("Right");
            rightLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String rightAxisValue = isUpdateMode ? String.valueOf(existingResep.getAxisKanan()) : "0";
            rightAxisField = new TextField(rightAxisValue);
            rightAxisField.setPrefWidth(inputWidth);
            rightAxisField.setPrefHeight(28);
            styleTextField(rightAxisField);
            rightBox.setFillWidth(true);
            rightBox.getChildren().addAll(rightLabel, rightAxisField);

            VBox leftBox = new VBox(2);
            Label leftLabel = new Label("Left");
            leftLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");
            String leftAxisValue = isUpdateMode ? String.valueOf(existingResep.getAxisKiri()) : "0";
            leftAxisField = new TextField(leftAxisValue);
            leftAxisField.setPrefWidth(inputWidth);
            leftAxisField.setPrefHeight(28);
            styleTextField(leftAxisField);
            leftBox.setFillWidth(true);
            leftBox.getChildren().addAll(leftLabel, leftAxisField);

            HBox.setHgrow(rightBox, Priority.ALWAYS);
            HBox.setHgrow(leftBox, Priority.ALWAYS);

            row.getChildren().addAll(rightBox, leftBox);
        }
        
        section.getChildren().addAll(sectionLabel, row);
        return section;
    }

    private VBox createSingleFieldSection(String label, double inputWidth) {
        VBox section = new VBox(4);
        Label sectionLabel = new Label(label);
        sectionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: rgba(36, 22, 80, 1);");

        String pdValue = isUpdateMode ? String.valueOf(existingResep.getPd()) : "63";
        pdField = new TextField(pdValue);
        pdField.setPrefWidth(inputWidth);
        pdField.setPrefHeight(28);
        pdField.setAlignment(Pos.CENTER);
        styleTextField(pdField);
        
        section.getChildren().addAll(sectionLabel, pdField);
        return section;
    }

    private void styleTextField(TextField textField) {
        textField.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #D1D5DB; " +
            "-fx-border-width: 1.5; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 12 16; " +
            "-fx-font-size: 15px; " +
            "-fx-text-fill: #374151; " +
            "-fx-alignment: center;"
        );

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #7B61FF; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-padding: 11 15; " +
                    "-fx-font-size: 15px; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-alignment: center;"
                );
            } else {
                styleTextField(textField);
            }
        });
    }

    private void collectTextFields(Parent parent, List<TextField> fields) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                fields.add((TextField) node);
            } else if (node instanceof Parent) {
                collectTextFields((Parent) node, fields);
            }
        }
    }

    // Save the prescription to the database
    private void savePrescriptionToDatabase(Reservasi reservasi, User currentOptometris) throws Exception {
        Resep resep = new Resep();
        
        // parse value dr text fields
        resep.setPlusKanan(Double.parseDouble(rightPlusField.getText()));
        resep.setPlusKiri(Double.parseDouble(leftPlusField.getText()));
        resep.setMinusKanan(Double.parseDouble(rightMinusField.getText()));
        resep.setMinusKiri(Double.parseDouble(leftMinusField.getText()));
        
        resep.setCylKanan(Double.parseDouble(rightCylField.getText()));
        resep.setCylKiri(Double.parseDouble(leftCylField.getText()));
        
        resep.setAxisKanan(Double.parseDouble(rightAxisField.getText()));
        resep.setAxisKiri(Double.parseDouble(leftAxisField.getText()));
        
        resep.setPd(Double.parseDouble(pdField.getText()));
        
        resep.setPelanggan(reservasi.getPelanggan());
        resep.setOptometris(currentOptometris);
        resep.setJadwal(reservasi.getJadwal());

        // Save ke database
        resepRepo.addResep(resep);
    }

    private void updatePrescriptionInDatabase(Reservasi reservasi, User currentOptometris) throws Exception {
        // Update resep yang udah ada
        existingResep.setPlusKanan(Double.parseDouble(rightPlusField.getText()));
        existingResep.setPlusKiri(Double.parseDouble(leftPlusField.getText()));
        existingResep.setMinusKanan(Double.parseDouble(rightMinusField.getText()));
        existingResep.setMinusKiri(Double.parseDouble(leftMinusField.getText()));
        
        existingResep.setCylKanan(Double.parseDouble(rightCylField.getText()));
        existingResep.setCylKiri(Double.parseDouble(leftCylField.getText()));
        
        existingResep.setAxisKanan(Double.parseDouble(rightAxisField.getText()));
        existingResep.setAxisKiri(Double.parseDouble(leftAxisField.getText()));
        
        existingResep.setPd(Double.parseDouble(pdField.getText()));

        resepRepo.updateResep(existingResep);
    }

    private void showSuccessAlert(String patientName, String action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Prescription for " + patientName + " has been " + action + " successfully!");
        alert.showAndWait();
    }
}