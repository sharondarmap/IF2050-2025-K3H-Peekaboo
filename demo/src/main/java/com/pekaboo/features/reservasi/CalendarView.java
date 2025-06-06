package com.pekaboo.features.reservasi;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.time.*;
import java.util.*;

public class CalendarView extends VBox {
    private YearMonth currentYearMonth;
    private GridPane calendarGrid;
    private Label monthLabel;
    private Map<LocalDate, Integer> slotTersedia; // key: tanggal, value: jumlah slot

    public CalendarView(Map<LocalDate, Integer> slotTersedia) {
        this.slotTersedia = slotTersedia;
        this.currentYearMonth = YearMonth.now();
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);

        HBox header = new HBox(10);
        Button prev = new Button("<");
        Button next = new Button(">");
        monthLabel = new Label();
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
        calendarGrid.setHgap(10);
        calendarGrid.setVgap(10);
        calendarGrid.setAlignment(Pos.CENTER);

        this.getChildren().addAll(header, calendarGrid);
        updateCalendar();
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        // Hari-hari dalam seminggu
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
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
            cell.setPrefSize(90, 80);

            Label dayNum = new Label(String.valueOf(day));
            cell.getChildren().add(dayNum);

            int slot = slotTersedia.getOrDefault(date, 0);
            if (slot > 0) {
                Label available = new Label("Available " + slot);
                available.setStyle("-fx-background-color: #C8F7C5; -fx-text-fill: #2E7D32; -fx-padding: 2 6 2 6; -fx-background-radius: 5;");
                Button reserveBtn = new Button("Reservation");
                reserveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 10px;");
                reserveBtn.setOnAction(e -> {
                    // TODO: aksi reservasi untuk tanggal ini
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reservasi untuk " + date);
                    alert.showAndWait();
                });
                cell.getChildren().addAll(available, reserveBtn);
            }

            cell.setStyle("-fx-background-color: #E5D8F6; -fx-border-color: #D1C4E9; -fx-border-radius: 8; -fx-background-radius: 8;");
            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
}