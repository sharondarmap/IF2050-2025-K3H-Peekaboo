package com.pekaboo.features.reservasi;

import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.util.*;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.repositories.JadwalRepository;

public class CalendarView extends VBox {
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthLabel;
    private Map<LocalDate, Integer> slotTersedia;
    private JadwalRepository jadwalRepo;

    public CalendarView(Map<LocalDate, Integer> slotTersedia, JadwalRepository jadwalRepo) {
        this.slotTersedia = slotTersedia;
        this.jadwalRepo = jadwalRepo;
        this.currentYearMonth = YearMonth.now();
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER); 

        this.setStyle(
            "-fx-background-color: #F5F5F5; " +     
            "-fx-padding: 12; " +              
            "-fx-background-radius: 12; " +   
            "-fx-border-color: #DDDDDD; " +   
            "-fx-border-width: 1; " +      
            "-fx-border-radius: 12; " +   
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);" // Subtle shadow
        );

        HBox header = new HBox(15);
        Button prev = new Button("<");
        Button next = new Button(">");
        monthLabel = new Label();
        prev.setStyle(
            "-fx-background-color: transparent; " +  
            "-fx-text-fill: rgba(36, 22, 80, 1); " +  
            "-fx-font-weight: bold; " +
            "-fx-font-size: 20px; " +
            "-fx-border-color: transparent; " +
            "-fx-pref-width: 30px; " +
            "-fx-pref-height: 30px; " +
            "-fx-cursor: hand;"
        );

        next.setStyle(
            "-fx-background-color: transparent; " + 
            "-fx-text-fill: rgba(36, 22, 80, 1); " + 
            "-fx-font-weight: bold; " +
            "-fx-font-size: 20px; " + 
            "-fx-border-color: transparent; " + 
            "-fx-pref-width: 30px; " +
            "-fx-pref-height: 30px; " +
            "-fx-cursor: hand;" 
        );
        monthLabel.setStyle(
            "-fx-font-size: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: rgba(36, 22, 80, 1);"
        );

        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(prev, monthLabel, next);

        prev.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });
        next.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        calendarGrid = new GridPane();
        calendarGrid.setHgap(8);
        calendarGrid.setVgap(8);
        calendarGrid.setAlignment(Pos.CENTER);

        this.getChildren().addAll(header, calendarGrid);
        updateCalendar();
    }

    private void updateCalendar() {
        slotTersedia = jadwalRepo.getSlotTersediaBulan(currentYearMonth);

        calendarGrid.getChildren().clear();
        monthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle(
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-text-fill: #666666; " +
                "-fx-padding: 10; " +
                "-fx-alignment: center;"
            );
            dayLabel.setPrefSize(120, 40);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int row = 1;
        int col = dayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox cell = new VBox(5);
            cell.setAlignment(Pos.TOP_CENTER);
            cell.setPrefSize(120, 90);
            cell.setPadding(new Insets(10, 8, 12,8));

            int slot = slotTersedia.getOrDefault(date, 0);

            Label dayNum = new Label(String.valueOf(day));
            dayNum.setStyle(
                "-fx-font-weight: bold; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: " + (slot > 0 ? "#333333" : "white") + ";"
            );
            cell.getChildren().add(dayNum);

            if (slot > 0) {
                Label available = new Label("Available " + slot);
                available.setStyle(
                    "-fx-background-color: rgba(37, 208, 65, 1); " +
                    "-fx-text-fill: white; " +
                    "-fx-padding: 2 6 2 6; " +      
                    "-fx-background-radius: 4; " +  
                    "-fx-font-size: 10px; " +       
                    "-fx-border-color: #C8F7C5; " +   
                    "-fx-border-radius: 4;"
                );
                Button reserveBtn = new Button("Reservation");
                reserveBtn.setStyle(
                    "-fx-background-color: rgba(54, 76, 132, 1); " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 9px; " +  
                    "-fx-padding: 3 8 3 8; " +    
                    "-fx-background-radius: 4; " + 
                    "-fx-pref-width: 70px; " + 
                    "-fx-pref-height: 22px;" 
                );
                reserveBtn.setOnAction(e -> {
                    showReservationDialog(date);
                });
                cell.getChildren().addAll(available, reserveBtn);
            }

            String cellStyle = slot > 0 ? 
                "-fx-background-color: white; -fx-border-color: #D1C4E9; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;" :
                "-fx-background-color: rgba(163, 142, 225, 1); -fx-border-color: #D1C4E9; -fx-border-radius: 8; -fx-background-radius: 8;";
            
            cell.setStyle(cellStyle);
            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void showReservationDialog(LocalDate selectedDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pekaboo/reservasi/confirmreservation.fxml"));
            Parent root = loader.load();

            ConfirmReservController controller = loader.getController();
            List<Jadwal> availableJadwal = jadwalRepo.getJadwalByTanggal(selectedDate);
            controller.setDate(selectedDate, availableJadwal);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirm Reservation");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
            
            if (controller.isConfirmed()) {
                // kl berhasil update slot
                slotTersedia = jadwalRepo.getSlotTersediaBulan(currentYearMonth);
                updateCalendar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}