package com.pekaboo.features.reservasi;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.StatusReservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.repositories.postgres.PostgresReservasiRepository;
import com.pekaboo.util.Session;

public class ConfirmReservController {
    @FXML private Label dateLabel;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private Label optometristLabel;
    
    private List<Jadwal> availableJadwal;
    private LocalDate selectedDate;
    private boolean confirmed = false;

    @FXML
    public void initialize() {
       timeComboBox.setOnAction(e -> updateOptometrist());
    }

    public void setDate(LocalDate date, List<Jadwal> jadwalList) {
        this.selectedDate = date;
        this.availableJadwal = jadwalList;
        dateLabel.setText(date.getDayOfWeek() + ", " + date.getDayOfMonth() + " " + date.getMonth());

        timeComboBox.getItems().clear();
        for (Jadwal jadwal : availableJadwal) {
            if (jadwal.isAvailable()) {
                String timeSlot = jadwal.getJamMulai() + " - " + jadwal.getJamSelesai();
                timeComboBox.getItems().add(timeSlot);
            }
        }

        if (!timeComboBox.getItems().isEmpty()) {
            timeComboBox.setValue(timeComboBox.getItems().get(0));
            updateOptometrist();
        }
    }

    private void updateOptometrist() {
        String selectedTime = timeComboBox.getValue();
        if (selectedTime != null && availableJadwal != null) {
            String jamMulai = selectedTime.split(" - ")[0];
            
            for (Jadwal jadwal : availableJadwal) {
                if (jadwal.getJamMulai().toString().equals(jamMulai)) {
                    // 🆕 Show simple optometrist info (no slot count)
                    String optometristInfo = String.format("👨‍⚕️ %s", jadwal.getOptometristName());
                    optometristLabel.setText(optometristInfo);
                    break;
                }
            }
        }
    }

    @FXML
    private void handleReserve() {
        // 🔧 Get current user from session
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showError("User not logged in", "Please login first to make a reservation.");
            return;
        }

        String selectedTime = timeComboBox.getValue();
        if (selectedTime != null && availableJadwal != null) {
            String jamMulai = selectedTime.split(" - ")[0];
            
            // Find selected jadwal
            for (Jadwal jadwal : availableJadwal) {
                if (jadwal.getJamMulai().toString().equals(jamMulai) && jadwal.isAvailable()) {
                    try {
                        // 🆕 Create reservasi object with User objects
                        Reservasi reservasi = new Reservasi();
                        reservasi.setStatusReservasi(StatusReservasi.CONFIRMED);
                        reservasi.setOptometris(jadwal.getOptometris()); // User object
                        reservasi.setPelanggan(currentUser); // User object
                        reservasi.setJadwal(jadwal); // Jadwal object
                        reservasi.setTanggalReservasi(LocalDateTime.now());
                        
                        // 🆕 Save to database
                        ReservasiRepository reservasiRepo = new PostgresReservasiRepository();
                        reservasiRepo.addReservasi(reservasi);
                        
                        confirmed = true;
                        
                        // Show success message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Reservation Confirmed");
                        alert.setContentText(String.format("Reservation confirmed for %s at %s with %s", 
                                                         selectedDate, selectedTime, jadwal.getOptometristName()));
                        alert.showAndWait();
                        
                        closeDialog();
                        return;
                        
                    } catch (Exception e) {
                        showError("Reservation Failed", "Failed to save reservation: " + e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        
        showError("Invalid Selection", "Please select a valid time slot.");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) dateLabel.getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}