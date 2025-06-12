package com.pekaboo.features.reservasi;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.pekaboo.App;
import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.StatusReservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.repositories.postgres.PostgresJadwalRepository;
import com.pekaboo.repositories.postgres.PostgresReservasiRepository;
import com.pekaboo.util.Session;

public class ReservasiController {
    @FXML private VBox calendarContainer;
    @FXML private HBox headerContainer;

    private final JadwalRepository jadwalRepo = new PostgresJadwalRepository();
    private final ReservasiRepository reservasiRepo = new PostgresReservasiRepository();
    private CalendarView calendarView;

    @FXML
    private void initialize() {
        addOperatingHours();
        
        YearMonth currentMonth = YearMonth.now();
        Map<LocalDate, Integer> slotTersedia = jadwalRepo.getSlotTersediaBulan(currentMonth);

        calendarView = new CalendarView(slotTersedia, jadwalRepo, this);
        calendarContainer.getChildren().add(calendarView);
    }

    public boolean handleReservation(LocalDate selectedDate, String selectedTime, List<Jadwal> availableJadwal) {
        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            showError("User not logged in", "Please login first to make a reservation.");
            return false;
        }

        if (selectedTime != null && availableJadwal != null) {
            String jamMulai = selectedTime.split(" - ")[0];
            
            for (Jadwal jadwal : availableJadwal) {
                if (jadwal.getJamMulai().toString().equals(jamMulai) && jadwal.isAvailable()) {
                    try {

                        Reservasi reservasi = new Reservasi();
                        reservasi.setStatusReservasi(StatusReservasi.CONFIRMED);
                        reservasi.setOptometris(jadwal.getOptometris());
                        reservasi.setPelanggan(currentUser);
                        reservasi.setJadwal(jadwal);
                        reservasi.setTanggalReservasi(LocalDateTime.now());
                        
                        reservasiRepo.addReservasi(reservasi);
                        
                        showSuccess("Reservation confirmed for " + selectedDate + " at " + selectedTime);
                        calendarView.refreshCalendar();
                        return true;
                        
                    } catch (Exception e) {
                        showError("Reservation Failed", "Failed to save reservation: " + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }
        
        showError("Invalid Selection", "Please select a valid time slot.");
        return false;
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Reservation Confirmed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean validateReservation(LocalDate date, String time) {
        if (date.isBefore(LocalDate.now())) {
            showError("Invalid Date", "Cannot make reservation for past dates.");
            return false;
        }
        
        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
        }
        
        return true;
    }

    private void addOperatingHours() {
        try {
            Image operatingHoursImg = new Image(
                getClass().getResourceAsStream("/com/pekaboo/reservasi/assets/operatinghours.png")
            );
            
            ImageView imageView = new ImageView(operatingHoursImg);
            imageView.setFitWidth(240);
            imageView.setPreserveRatio(true);
            
            headerContainer.setAlignment(Pos.CENTER_LEFT);
            headerContainer.setPadding(new Insets(0, 0, 20, 0));
            headerContainer.getChildren().add(imageView);
            
        } catch (Exception e) {
            System.err.println("Could not load operating hours image: " + e.getMessage());
        }
    }
}