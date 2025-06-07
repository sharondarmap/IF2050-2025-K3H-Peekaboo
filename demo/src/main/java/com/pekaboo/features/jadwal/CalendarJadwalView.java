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
    private final StackPane rootStack;


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
        VBox overlay = new VBox(10);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(20));
        overlay.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

        Label title = new Label("Tambah Jadwal - " + tanggal);
        TextField jamMulai = new TextField("10:00");
        TextField jamSelesai = new TextField("11:00");
        Button simpan = new Button("Simpan");
        Button batal = new Button("Batal");

        HBox actions = new HBox(10, simpan, batal);
        actions.setAlignment(Pos.CENTER);

        overlay.getChildren().addAll(title, new Label("Jam Mulai:"), jamMulai, new Label("Jam Selesai:"), jamSelesai, actions);

        VBox dimmer = new VBox(overlay);
        dimmer.setAlignment(Pos.CENTER);
        dimmer.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        dimmer.setPrefSize(rootStack.getWidth(), rootStack.getHeight());

        rootStack.getChildren().add(dimmer);

        batal.setOnAction(e -> rootStack.getChildren().remove(dimmer));
        simpan.setOnAction(e -> {
            try {
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
        VBox overlay = new VBox(12);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(20));
        overlay.setMaxWidth(300);
        overlay.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        Label title = new Label("Detail Jadwal");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #241650;");

        Label dateLbl = new Label("Tanggal: " + jadwal.getTanggal());
        Label startLbl = new Label("Jam Mulai:");
        TextField startField = new TextField(jadwal.getJamMulai().toString());
        Label endLbl = new Label("Jam Selesai:");
        TextField endField = new TextField(jadwal.getJamSelesai().toString());

        HBox actionBox = new HBox(10);
        Button simpanBtn = new Button("Simpan");
        Button hapusBtn = new Button("Hapus");
        Button batalBtn = new Button("Batal");

        simpanBtn.setStyle("-fx-background-color: #364C84; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 5;");
        hapusBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 5;");
        batalBtn.setStyle("-fx-background-color: #ccc; -fx-text-fill: #333; -fx-padding: 8 16; -fx-background-radius: 5;");
        actionBox.getChildren().addAll(simpanBtn, hapusBtn, batalBtn);
        actionBox.setAlignment(Pos.CENTER);

        overlay.getChildren().addAll(
            title, dateLbl,
            startLbl, startField,
            endLbl, endField,
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
                Alert alert = new Alert(Alert.AlertType.WARNING, "Jadwal sudah dipesan dan tidak bisa dihapus.", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

}
