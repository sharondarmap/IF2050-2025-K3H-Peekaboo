package com.pekaboo.features.reservasi;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
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
    private ReservasiController controller;

    private StackPane mainContainer;
    private VBox popupContainer;
    private boolean isPopupVisible = false;

    public CalendarView(Map<LocalDate, Integer> slotTersedia, JadwalRepository jadwalRepo, ReservasiController controller) {
        this.slotTersedia = slotTersedia;
        this.jadwalRepo = jadwalRepo;
        this.controller = controller;
        this.currentYearMonth = YearMonth.now();

        mainContainer = new StackPane();
        VBox calendarContainer = createCalendar();
        popupContainer = createPopup();
        popupContainer.setVisible(false);
        
        mainContainer.getChildren().addAll(calendarContainer, popupContainer);
        this.getChildren().add(mainContainer);
    }

    public void refreshCalendar() {
        updateCalendar();
    }
    
    private VBox createCalendar() {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        
        container.setStyle(
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
                if (!isPopupVisible) {
                    currentYearMonth = currentYearMonth.minusMonths(1);
                    updateCalendar();
                }
            });
            next.setOnAction(e -> {
                if (!isPopupVisible) {
                    currentYearMonth = currentYearMonth.plusMonths(1);
                    updateCalendar();
                }
            });

            calendarGrid = new GridPane();
            calendarGrid.setHgap(8);
            calendarGrid.setVgap(8);
            calendarGrid.setAlignment(Pos.CENTER);

            container.getChildren().addAll(header, calendarGrid);
            updateCalendar();
        
            return container;
    }

    private VBox createPopup() {
        VBox overlay = new VBox();
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.5);" // semi transparan background
        );
        
        VBox popup = new VBox(15);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(20));
        popup.setMaxWidth(400);
        popup.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 12; " +
            "-fx-border-color: #DDDDDD; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 15, 0, 0, 5);"
        );

        Label titleLabel = new Label("Confirm Reservation");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label warningLabel = new Label("Reservations cannot be canceled! Please choose an appropriate time.");
        warningLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-size: 12px;");
        warningLabel.setWrapText(true);
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        warningLabel.setMaxWidth(Double.MAX_VALUE);

        VBox dateSection = new VBox(5);
        Label dateFieldLabel = new Label("Date");
        dateFieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label dateValueLabel = new Label();
        dateValueLabel.setStyle("-fx-font-size: 14px;");
        dateSection.getChildren().addAll(dateFieldLabel, dateValueLabel);
        
        VBox timeSection = new VBox(5);
        Label timeFieldLabel = new Label("Start Time");
        timeFieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.setPromptText("Select time");
        timeSection.getChildren().addAll(timeFieldLabel, timeComboBox);
        
        VBox optometristSection = new VBox(5);
        Label optometristFieldLabel = new Label("Optometrist");
        optometristFieldLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label optometristValueLabel = new Label();
        optometristValueLabel.setStyle("-fx-font-size: 14px;");
        optometristSection.getChildren().addAll(optometristFieldLabel, optometristValueLabel);
        
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        Button cancelButton = new Button("Cancel");
        Button reserveButton = new Button("Reserve");
        
        cancelButton.setStyle(
            "-fx-background-color: #ddd; " +
            "-fx-text-fill: #333; " +
            "-fx-padding: 8 16; " +
            "-fx-background-radius: 5;"
        );
        
        reserveButton.setStyle(
            "-fx-background-color: #5a29e4; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 8 16; " +
            "-fx-background-radius: 5;"
        );
        
        buttonBox.getChildren().addAll(cancelButton, reserveButton);
        
        popup.getChildren().addAll(
            titleLabel, warningLabel, dateSection, 
            timeSection, optometristSection, buttonBox
        );
        
        popup.setUserData(Map.of(
            "dateLabel", dateValueLabel,
            "timeComboBox", timeComboBox,
            "optometristLabel", optometristValueLabel,
            "cancelButton", cancelButton,
            "reserveButton", reserveButton
        ));
        
        overlay.getChildren().add(popup);
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) {
                hidePopup();
            }
        });
        
        return overlay;
    }

    private void showReservationDialog(LocalDate selectedDate) {
        @SuppressWarnings("unchecked")
        Map<String, Object> components = (Map<String, Object>) popupContainer.getChildren().get(0).getUserData();
        
        Label dateLabel = (Label) components.get("dateLabel");
        ComboBox<String> timeComboBox = (ComboBox<String>) components.get("timeComboBox");
        Label optometristLabel = (Label) components.get("optometristLabel");
        Button cancelButton = (Button) components.get("cancelButton");
        Button reserveButton = (Button) components.get("reserveButton");
        
        dateLabel.setText(selectedDate.getDayOfWeek() + ", " + selectedDate.getDayOfMonth() + " " + selectedDate.getMonth());
        
        List<Jadwal> availableJadwal = jadwalRepo.getJadwalByTanggal(selectedDate);
        timeComboBox.getItems().clear();
        
        for (Jadwal jadwal : availableJadwal) {
            if (jadwal.isAvailable()) {
                String timeSlot = jadwal.getJamMulai() + " - " + jadwal.getJamSelesai();
                timeComboBox.getItems().add(timeSlot);
            }
        }
        
        if (!timeComboBox.getItems().isEmpty()) {
            timeComboBox.setValue(timeComboBox.getItems().get(0));
            updateOptometristInfo(timeComboBox.getValue(), availableJadwal, optometristLabel);
        }
        
        timeComboBox.setOnAction(e -> {
            updateOptometristInfo(timeComboBox.getValue(), availableJadwal, optometristLabel);
        });
        
        cancelButton.setOnAction(e -> hidePopup());
        
        reserveButton.setOnAction(e -> {
            if (controller.validateReservation(selectedDate, timeComboBox.getValue())) {
                if (controller.handleReservation(selectedDate, timeComboBox.getValue(), availableJadwal)) {
                    hidePopup();
                }
            }
        });
        
        isPopupVisible = true;
        popupContainer.setVisible(true);
    }

    private void hidePopup() {
        isPopupVisible = false;
        popupContainer.setVisible(false);
    }

    private void updateOptometristInfo(String selectedTime, List<Jadwal> availableJadwal, Label optometristLabel) {
        if (selectedTime != null && availableJadwal != null) {
            String jamMulai = selectedTime.split(" - ")[0];
            
            for (Jadwal jadwal : availableJadwal) {
                if (jadwal.getJamMulai().toString().equals(jamMulai)) {
                    String optometristInfo = String.format("👨‍⚕️ %s", jadwal.getOptometristName());
                    optometristLabel.setText(optometristInfo);
                    break;
                }
            }
        }
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
                    if (!isPopupVisible) {
                        showReservationDialog(date);
                    }
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
}