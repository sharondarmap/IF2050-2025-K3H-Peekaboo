package com.pekaboo.features.reservasi;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import com.pekaboo.App;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.repositories.postgres.PostgresJadwalRepository;
import com.pekaboo.repositories.postgres.PostgresReservasiRepository;

public class ReservasiController {
    @FXML private VBox calendarContainer;

    //private final ReservasiRepository reservasiRepo = new PostgresReservasiRepository();

    @FXML
    private void initialize() {
        // TODO: ini nanti diganti pake data jadwal dari database
        // Map<LocalDate, Integer> slotTersedia = new HashMap<>();
        // YearMonth ym = YearMonth.now();
        // for (int i = 1; i <= ym.lengthOfMonth(); i++) {
        //     LocalDate tgl = ym.atDay(i);
        //     if (Math.random() > 0.5) slotTersedia.put(tgl, (int)(Math.random() * 6) + 1);
        // }
        JadwalRepository jadwalRepo = new PostgresJadwalRepository();
        YearMonth currentMonth = YearMonth.now();
        Map<LocalDate, Integer> slotTersedia = jadwalRepo.getSlotTersediaBulan(currentMonth);

        CalendarView calendarView = new CalendarView(slotTersedia, jadwalRepo);
        calendarContainer.getChildren().add(calendarView);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}