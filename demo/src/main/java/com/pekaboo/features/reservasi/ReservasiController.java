package com.pekaboo.features.reservasi;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class ReservasiController {
    @FXML private VBox calendarContainer;

    @FXML
    private void initialize() {
        Map<LocalDate, Integer> slotTersedia = new HashMap<>();
        YearMonth ym = YearMonth.now();
        for (int i = 1; i <= ym.lengthOfMonth(); i++) {
            LocalDate tgl = ym.atDay(i);
            if (Math.random() > 0.5) slotTersedia.put(tgl, (int)(Math.random() * 6) + 1);
        }
        CalendarView calendarView = new CalendarView(slotTersedia);
        calendarContainer.getChildren().add(calendarView);
    }
}