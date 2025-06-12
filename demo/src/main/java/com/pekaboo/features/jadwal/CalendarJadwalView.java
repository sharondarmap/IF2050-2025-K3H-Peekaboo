package com.pekaboo.features.jadwal;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.Resep;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.StatusReservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.repositories.ResepRepository;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.repositories.postgres.PostgresResepRepository;
import com.pekaboo.repositories.postgres.PostgresReservasiRepository;
import com.pekaboo.features.resep.ResepController;

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
    private final StackPane rootStack;

    private final ReservasiRepository reservasiRepo = new PostgresReservasiRepository();
    private final ResepController resepController = new ResepController();

    public CalendarJadwalView(JadwalRepository jadwalRepo, User currentOptometris, StackPane rootStack) {
        this.jadwalRepo = jadwalRepo;
        this.currentOptometris = currentOptometris;
        this.currentMonth = YearMonth.now();
        this.rootStack = rootStack;

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
            dayCell.setPickOnBounds(false); //ini ngehindarin kalau diclick bisa berubah stylenya
            dayCell.setPrefSize(140, 120);
            dayCell.setPadding(new Insets(10));
            dayCell.setFocusTraversable(false);

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setFont(new Font(20));
            dayLabel.setFocusTraversable(false);
            dayLabel.setStyle(
                "-fx-text-fill: #241650;" +
                "-fx-font-weight: 700;" +
                "-fx-font-size: 20px;" +
                "-fx-alignment: center;" +
                "-fx-background-insets: 0;" +  // prevents extra padding from focus ring
                "-fx-effect: none;"            // removes glow/focus effects
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
            addBtn.setOnAction(e -> openAddJadwalOverlay(tanggal));

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
                    detailBtn.setOnAction(e -> openDetailOverlay(j));
                    dayCell.getChildren().add(detailBtn);
                }
            }

            // Set background color based on whether there are schedules
            String backgroundColor = jadwalHariIni.isEmpty() ? "#FFFFFF" : "#E2E2E2";
            dayCell.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-font-size: 12px;" +
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

    private void openAddJadwalOverlay(LocalDate tanggal) {
        VBox overlay = new VBox(12);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(24, 40, 24, 40));
        overlay.setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 8);");

        overlay.setMaxWidth(400);
        overlay.setPrefWidth(400);
        overlay.setMinWidth(350);

        Label title = new Label("Add new jadwal");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: rgba(36, 22, 80, 1); -fx-alignment: center;");
        Label dateInfo = new Label("Date: " + tanggal);
        dateInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: 500; -fx-text-fill: rgba(50, 30, 110, 1); -fx-alignment: center; -fx-padding: 0;");
        Label startTimeLbl = new Label("Start Time:");
        startTimeLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: rgba(36, 22, 80, 1); -fx-padding: 4 0 2 0;");
        Label endTimeLbl = new Label("End Time:");
        endTimeLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: rgba(36, 22, 80, 1); -fx-padding: 4 0 2 0;");
        
        TextField jamMulai = new TextField("10:00");
        TextField jamSelesai = new TextField("11:00");
        jamMulai.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 12; " +
            "-fx-background-color: #F8FAFC; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-min-height: 36px;"
        );
        jamSelesai.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-padding: 10 12; " +
            "-fx-background-color: #F8FAFC; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-min-height: 36px;"
        );

        Button simpan = new Button("Save");
        Button batal = new Button("Cancel");
        simpan.setPrefWidth(150);
        simpan.setPrefHeight(36);
        simpan.setStyle(
            "-fx-background-color: rgba(91, 54, 201, 1); " +
            "-fx-text-fill: rgba(255, 255, 255, 1); " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 700; " +
            "-fx-font-size: 14px;"
        );
        
        batal.setPrefWidth(150);
        batal.setPrefHeight(36);
        batal.setStyle(
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

        HBox actions = new HBox(16);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(batal, simpan);
        HBox.setHgrow(batal, Priority.ALWAYS);
        HBox.setHgrow(simpan, Priority.ALWAYS);

        Region spacer = new Region();
        spacer.setPrefHeight(16);
        overlay.getChildren().addAll(title, dateInfo, startTimeLbl, jamMulai, endTimeLbl, jamSelesai, spacer, actions);

        VBox dimmer = new VBox(overlay);
        dimmer.setAlignment(Pos.CENTER);
        dimmer.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        dimmer.setPrefSize(rootStack.getWidth(), rootStack.getHeight());

        rootStack.getChildren().add(dimmer);

        batal.setOnAction(e -> rootStack.getChildren().remove(dimmer));
        simpan.setOnAction(e -> {
            try {
                if (tanggal.isBefore(LocalDate.now())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText("Cannot Create Schedule");
                    alert.setContentText("Cannot create schedule for past dates.");
                    alert.showAndWait();
                    return;
                }
                    
                LocalTime startTime = LocalTime.parse(jamMulai.getText());
                LocalTime endTime = LocalTime.parse(jamSelesai.getText());
                
                if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Time");
                    alert.setHeaderText("Invalid Schedule Time");
                    alert.setContentText("Start time must be before end time.");
                    alert.showAndWait();
                    return;
                }
                Jadwal j = new Jadwal();
                j.setTanggal(tanggal);
                j.setJamMulai(LocalTime.parse(jamMulai.getText()));
                j.setJamSelesai(LocalTime.parse(jamSelesai.getText()));
                j.setOptometris(currentOptometris);
                j.setStatusJadwal(StatusJadwal.AVAILABLE);

                jadwalRepo.addJadwal(j);
                updateCalendar();
                rootStack.getChildren().remove(dimmer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openDetailOverlay(Jadwal jadwal) {
        VBox overlay = new VBox(8);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(24, 40, 24, 40));
        overlay.setMaxWidth(400);
        overlay.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 8);"
        );

        Label title = new Label("Schedule Details");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: rgba(36, 22, 80, 1);");

        Label dateLbl = new Label("Date: " + jadwal.getTanggal());
        dateLbl.setStyle(
            "-fx-font-size: 16px; " +  
            "-fx-font-weight: 600; " + 
            "-fx-text-fill: rgba(50, 30, 110, 1); " +
            "-fx-font-weight: 500; " +
            "-fx-padding: 0; " +  
            "-fx-min-height: 12px; " +  
            "-fx-alignment: center-left;" 
        );

        VBox timeSection = new VBox(8);
        timeSection.setAlignment(Pos.CENTER_LEFT);
        
        Label startLbl = new Label("Start Time:");
        startLbl.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +
            "-fx-text-fill: rgba(36, 22, 80, 1); " +
            "-fx-padding: 4 0 2 0;" 
        );
        
        TextField startField = new TextField(jadwal.getJamMulai().toString());
        startField.setStyle(
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
        
        Label endLbl = new Label("End Time:");
        endLbl.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: 600; " +  
            "-fx-text-fill: rgba(36, 22, 80, 1); " +
            "-fx-padding: 4 0 2 0;"  
        );
        
        TextField endField = new TextField(jadwal.getJamSelesai().toString());
        endField.setStyle(
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
        
        timeSection.getChildren().addAll(startLbl, startField, endLbl, endField);

        Reservasi reservasi = reservasiRepo.getReservasiByJadwal(jadwal);
    
        VBox reservationSection = new VBox(15);
        if (reservasi != null && reservasi.getStatusReservasi() != StatusReservasi.CANCELLED) {
            Label reservationTitle = new Label("📋 Reservation Details");
            reservationTitle.setStyle(
                "-fx-font-weight: 600; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: rgba(36, 22, 80, 1);"
            );

            VBox infoContainer = new VBox(8);
            infoContainer.setPadding(new Insets(8, 16, 8, 16));
            infoContainer.setStyle(
                "-fx-background-color: #F8FAFC; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #E2E8F0; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);"
            );
            
            VBox customerInfo = new VBox(0);
            Label customerLabel = new Label("Customer");
            customerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-font-weight: 500;");

            Label customerName = new Label(reservasi.getPelanggan().getUsername());
            customerName.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(36, 22, 80, 1); -fx-font-weight: 600;");
            customerInfo.getChildren().addAll(customerLabel, customerName);

            VBox statusInfo = new VBox(0);
            Label statusLabel = new Label("Status");
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-font-weight: 500;");

            Label statusValue = new Label(reservasi.getStatusReservasi().name());
            statusValue.setStyle("-fx-font-size: 14px; -fx-text-fill: #10B981; -fx-font-weight: 600;");
            statusInfo.getChildren().addAll(statusLabel, statusValue);

            VBox dateInfo = new VBox(0);
            Label dateLabel = new Label("Reservation Date");
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-font-weight: 500;");

            Label dateValue = new Label(reservasi.getTanggalReservasi().toLocalDate().toString());
            dateValue.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(36, 22, 80, 1); -fx-font-weight: 600;");
            dateInfo.getChildren().addAll(dateLabel, dateValue);

            infoContainer.getChildren().addAll(customerInfo, statusInfo, dateInfo);
            
            ResepRepository resepRepo = new PostgresResepRepository();
            Resep existingResep = resepRepo.getResepByJadwal(reservasi.getJadwal());

            // prescription button
            Button addPrescriptionBtn = new Button(existingResep != null ? "Update Prescription" : "Add Prescription");
            addPrescriptionBtn.setPrefWidth(350);
            addPrescriptionBtn.setPrefHeight(45);
            addPrescriptionBtn.setStyle(
                "-fx-background-color: rgba(91, 54, 201, 1); " +
                "-fx-text-fill: rgba(255, 255, 255, 1); " +
                "-fx-padding: 8 24; " +
                "-fx-background-radius: 10; " +
                "-fx-font-weight: 700; " +
                "-fx-font-size: 14px; "
            );

            addPrescriptionBtn.setOnAction(e -> {
                rootStack.getChildren().removeIf(node -> node.getStyle().contains("rgba(0,0,0,0.5)"));
                Resep resepInAction = resepRepo.getResepByJadwal(reservasi.getJadwal());
                
                if (resepInAction != null) {
                    resepController.showUpdatePrescriptionOverlay(reservasi, rootStack, currentOptometris, resepInAction);
                } else {
                    resepController.showAddPrescriptionOverlay(reservasi, rootStack, currentOptometris);
                }
            });

            HBox buttonContainer = new HBox();
            buttonContainer.setAlignment(Pos.CENTER);
            buttonContainer.setPadding(new Insets(8, 0, 4, 0));
            buttonContainer.getChildren().add(addPrescriptionBtn);

            reservationSection.getChildren().addAll(reservationTitle, infoContainer, buttonContainer);
            
        } else {
            VBox emptyState = new VBox(10);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(25));
            emptyState.setStyle(
                "-fx-background-color: #F9FAFB; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #E5E7EB; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);"
            );
            
            Label emptyIcon = new Label("📭");
            emptyIcon.setStyle("-fx-font-size: 28px;");
            
            Label emptyTitle = new Label("No Reservations Yet");
            emptyTitle.setStyle(
                "-fx-font-weight: 600; " +
                "-fx-font-size: 16px; " +
                "-fx-text-fill: rgba(36, 22, 80, 1);"
            );
            
            Label emptyDesc = new Label("This schedule is not yet booked");
            emptyDesc.setStyle(
                "-fx-font-size: 13px; " +
                "-fx-text-fill: #9CA3AF; " +
                "-fx-text-alignment: center;"
            );
            emptyDesc.setWrapText(true);
            
            emptyState.getChildren().addAll(emptyIcon, emptyTitle, emptyDesc);
            reservationSection.getChildren().add(emptyState);
        }

        HBox actionBox = new HBox(8);
        Button simpanBtn = new Button("Save");
        Button hapusBtn = new Button("Delete");
        Button batalBtn = new Button("Cancel");

        simpanBtn.setPrefWidth(110);
        simpanBtn.setPrefHeight(36);
        simpanBtn.setStyle(
            "-fx-background-color: rgba(36, 22, 80, 1); " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 600; " +
            "-fx-font-size: 14px; "
        );

        hapusBtn.setPrefWidth(110);
        hapusBtn.setPrefHeight(36);
        hapusBtn.setStyle(
            "-fx-background-color: #DC2626; " + 
            "-fx-text-fill: white; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 600; " +
            "-fx-font-size: 14px; "
        );

        batalBtn.setPrefWidth(110);
        batalBtn.setPrefHeight(36);
        batalBtn.setStyle(
            "-fx-background-color: #F3F4F6; " + 
            "-fx-text-fill: #6B7280; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-weight: 600; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand; " +
            "-fx-border-color: #E5E7EB; " + 
            "-fx-border-width: 1; " +
            "-fx-border-radius: 8;"
        );
        actionBox.getChildren().addAll(simpanBtn, hapusBtn, batalBtn);
        actionBox.setAlignment(Pos.CENTER);

        overlay.getChildren().addAll(
            title, dateLbl,
            timeSection,
            new Region() {{ setPrefHeight(8); }},
            new Separator(),
            reservationSection,
            actionBox
        );

        VBox dimmer = new VBox(overlay);
        dimmer.setAlignment(Pos.CENTER);
        dimmer.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        dimmer.setPrefSize(rootStack.getWidth(), rootStack.getHeight());

        rootStack.getChildren().add(dimmer);

        batalBtn.setOnAction(e -> rootStack.getChildren().remove(dimmer));

        simpanBtn.setOnAction(e -> {
            try {
                if (!jadwal.isAvailable()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot update jadwal that is already reserved.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                jadwal.setJamMulai(LocalTime.parse(startField.getText()));
                jadwal.setJamSelesai(LocalTime.parse(endField.getText()));
                jadwalRepo.updateJadwal(jadwal);
                updateCalendar();
                rootStack.getChildren().remove(dimmer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        hapusBtn.setOnAction(e -> {
            if (jadwal.isAvailable()) {
                jadwalRepo.deleteJadwal(jadwal.getIdJadwal());
                updateCalendar();
                rootStack.getChildren().remove(dimmer);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot delete jadwal that is already reserved.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

}
