package com.pekaboo.features.jadwal;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.repositories.postgres.PostgresJadwalRepository;
import com.pekaboo.util.Session;

public class JadwalController {

@FXML 
private StackPane calendarContainer;


    private JadwalRepository jadwalRepo;

    // Simulasi current logged-in optometris (harusnya diambil dari sesi login)
    private User currentOptometris;

    @FXML
    private void initialize() {
        jadwalRepo = new PostgresJadwalRepository();
        currentOptometris = getLoggedInUser();

        CalendarJadwalView calendarView = new CalendarJadwalView(jadwalRepo, currentOptometris, calendarContainer);
        calendarContainer.getChildren().add(calendarView);
    }

    private User getLoggedInUser() {
        // Dummy user, nanti ganti ini dengan user dari session login
        return Session.getCurrentUser();
    }
}
