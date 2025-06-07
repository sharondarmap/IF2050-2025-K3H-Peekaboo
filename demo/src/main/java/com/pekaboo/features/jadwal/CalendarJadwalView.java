package com.pekaboo.features.jadwal;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarJadwalView extends VBox {
    private final JadwalRepository jadwalRepo;
    private final User currentOptometris;
    private YearMonth currentMonth;
    private final GridPane calendarGrid;
    private final Label monthLabel;

    public CalendarJadwalView(JadwalRepository jadwalRepo, User currentOptometris) {
        this.jadwalRepo = jadwalRepo;
        this.currentOptometris = currentOptometris;
        this.currentMonth = YearMonth.now();

        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-padding: 12;" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: #DDDDDD;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Header navigation
        HBox header = new HBox(15);
        Button prev = new Button("<");
        Button next = new Button(">");
        monthLabel = new Label();
        monthLabel.setFont(new Font(18));
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

        prev.setOnAction(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        next.setOnAction(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(prev, monthLabel, next);

        calendarGrid = new GridPane();
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);

        this.getChildren().addAll(header, calendarGrid);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentMonth.getMonth().toString() + " " + currentMonth.getYear());

        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        List<Jadwal> allJadwal = jadwalRepo.getAllJadwal();

        int row = 0, col = dayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate tanggal = currentMonth.atDay(day);
            VBox dayCell = new VBox(5);
            dayCell.setPrefSize(140, 120);
            dayCell.setPadding(new Insets(10));

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setFont(new Font(20));
            dayLabel.setStyle(
                "-fx-text-fill: #241650;" +
                "-fx-font-weight: 700;" +
                "-fx-alignment: center;"
            );
            
            // Create a container for the day label to keep it top-right
            HBox dayLabelContainer = new HBox();
            dayLabelContainer.setAlignment(Pos.CENTER_RIGHT);
            dayLabelContainer.getChildren().add(dayLabel);
            dayCell.getChildren().add(dayLabelContainer);

            Button addBtn = new Button("+ Jadwal");
            addBtn.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-background-color: #ddd;" +
                "-fx-text-fill: #333;" +
                "-fx-background-radius: 5;" +
                "-fx-cursor: hand;"
            );
            addBtn.setOnAction(e -> openAddJadwalDialog(tanggal));

            List<Jadwal> jadwalHariIni = allJadwal.stream()
                .filter(j -> j.getTanggal().equals(tanggal) &&
                            j.getOptometris().getIdUser() == currentOptometris.getIdUser())
                .collect(Collectors.toList());

            if (jadwalHariIni.isEmpty()) {
                // When no schedules exist, center the add button
                dayCell.setAlignment(Pos.TOP_CENTER);
                
                // Add spacer to push the add button to center
                Region spacer = new Region();
                VBox.setVgrow(spacer, Priority.ALWAYS);
                dayCell.getChildren().add(spacer);
                
                dayCell.getChildren().add(addBtn); // Add button in center
                
                Region spacer2 = new Region(); // Add another spacer to balance
                VBox.setVgrow(spacer2, Priority.ALWAYS);
                dayCell.getChildren().add(spacer2);
            } else {
                dayCell.setAlignment(Pos.TOP_LEFT);// When schedules exist, align content to top
                dayCell.getChildren().add(addBtn);
                
                for (Jadwal j : jadwalHariIni) {
                    Button detailBtn = new Button(j.getJamMulai() + " - " + j.getJamSelesai());
                    detailBtn.setStyle(
                        "-fx-padding: 12 16 12 16;" +
                        "-fx-background-color: #364C84;" +
                        "-fx-background-radius: 4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: 400;" +
                        "-fx-pref-width: 89px;"
                    );
                    detailBtn.setOnAction(e -> openDetailDialog(j));
                    dayCell.getChildren().add(detailBtn);
                }
            }

            // Set background color based on whether there are schedules
            String backgroundColor = jadwalHariIni.isEmpty() ? "#FFFFFF" : "#E2E2E2";
            dayCell.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: transparent;" +
                "-fx-position: relative;"
            );

            calendarGrid.add(dayCell, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void openAddJadwalDialog(LocalDate tanggal) {
        Dialog<Jadwal> dialog = new Dialog<>();
        dialog.setTitle("Add Jadwal - " + tanggal);

        Label jamMulaiLbl = new Label("Jam Mulai:");
        TextField jamMulaiField = new TextField("10:00");
        Label jamSelesaiLbl = new Label("Jam Selesai:");
        TextField jamSelesaiField = new TextField("11:00");

        VBox content = new VBox(10, jamMulaiLbl, jamMulaiField, jamSelesaiLbl, jamSelesaiField);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    LocalTime mulai = LocalTime.parse(jamMulaiField.getText());
                    LocalTime selesai = LocalTime.parse(jamSelesaiField.getText());

                    Jadwal newJadwal = new Jadwal();
                    newJadwal.setTanggal(tanggal);
                    newJadwal.setJamMulai(mulai);
                    newJadwal.setJamSelesai(selesai);
                    newJadwal.setOptometris(currentOptometris);
                    newJadwal.setStatusJadwal(StatusJadwal.AVAILABLE);

                    jadwalRepo.addJadwal(newJadwal);
                    updateCalendar();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void openDetailDialog(Jadwal jadwal) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Jadwal");

        Label dateLbl = new Label("Tanggal: " + jadwal.getTanggal());
        Label startLbl = new Label("Jam Mulai:");
        TextField startField = new TextField(jadwal.getJamMulai().toString());
        Label endLbl = new Label("Jam Selesai:");
        TextField endField = new TextField(jadwal.getJamSelesai().toString());

        ButtonType editBtnType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteBtnType = new ButtonType("Hapus", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(editBtnType, deleteBtnType, ButtonType.CANCEL);

        VBox content = new VBox(10, dateLbl, startLbl, startField, endLbl, endField);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == editBtnType) {
                try {
                    jadwal.setJamMulai(LocalTime.parse(startField.getText()));
                    jadwal.setJamSelesai(LocalTime.parse(endField.getText()));
                    jadwalRepo.updateJadwal(jadwal);
                    updateCalendar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (button == deleteBtnType) {
                if (jadwal.isAvailable()) {
                    jadwalRepo.deleteJadwal(jadwal.getIdJadwal());
                    updateCalendar();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Jadwal sudah dipesan dan tidak bisa dihapus.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}
