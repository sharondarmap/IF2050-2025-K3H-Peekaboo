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
    private final YearMonth currentMonth;
    private final User currentOptometris;
    private final GridPane calendarGrid;

    public CalendarJadwalView(JadwalRepository jadwalRepo, User currentOptometris) {
        this.jadwalRepo = jadwalRepo;
        this.currentOptometris = currentOptometris;
        this.currentMonth = YearMonth.now();
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Manage Jadwal - " + currentMonth.getMonth() + " " + currentMonth.getYear());
        title.setFont(new Font(20));
        this.getChildren().add(title);

        calendarGrid = new GridPane();
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);

        this.getChildren().add(calendarGrid);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
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
            dayCell.setStyle("-fx-border-color: #ccc; -fx-background-radius: 6; -fx-border-radius: 6;");
            dayCell.setAlignment(Pos.TOP_LEFT);

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setFont(new Font(16));
            dayCell.getChildren().add(dayLabel);

            Button addBtn = new Button("+ Jadwal");
            addBtn.setOnAction(e -> openAddJadwalDialog(tanggal));
            dayCell.getChildren().add(addBtn);

            List<Jadwal> jadwalHariIni = allJadwal.stream()
                .filter(j -> j.getTanggal().equals(tanggal) &&
                             j.getOptometris().getIdUser() == currentOptometris.getIdUser())
                .collect(Collectors.toList());

            for (Jadwal j : jadwalHariIni) {
                Button detailBtn = new Button(j.getJamMulai() + " - " + j.getJamSelesai());
                detailBtn.setStyle("-fx-font-size: 10px;");
                detailBtn.setOnAction(e -> openDetailDialog(j));
                dayCell.getChildren().add(detailBtn);
            }

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
