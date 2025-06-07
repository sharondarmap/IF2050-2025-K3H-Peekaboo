package com.pekaboo.auth;

import com.pekaboo.entities.User;
import com.pekaboo.repositories.UserRepository;
import com.pekaboo.repositories.postgres.PostgresUserRepository;
import com.pekaboo.util.Session;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class AuthController {

    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Button loginButton;

    // @FXML private TextField registerUsernameField;
    // @FXML private PasswordField registerPasswordField;
    // @FXML private TextField emailField;
    // @FXML private ChoiceBox<String> userRoleBox;
    // @FXML private DatePicker birthDatePicker;

    private final UserRepository userRepo = new PostgresUserRepository();

    // @FXML
    // public void initialize() {
    //     userRoleBox.getItems().addAll("PELANGGAN", "ADMIN", "OPTOMETRIS");
    // }

    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        User user = userRepo.getUserByCredentials(username, password);
        if (user != null) {
            Session.setCurrentUser(user);
            // Navigate to dashboard
        } else {
            showAlert("Login failed", "Username or password incorrect");
        }
    }

    // @FXML
    // private void handleRegister() {
    //     User newUser = new User();
    //     newUser.setUsername(registerUsernameField.getText());
    //     newUser.setPassword(registerPasswordField.getText()); // hash later
    //     newUser.setEmail(emailField.getText());
    //     newUser.setTanggalLahir(birthDatePicker.getValue());
    //     newUser.setUserStatus(userRoleBox.getValue());
    //     //more fields later

    //     userRepo.saveUser(newUser);
    //     showAlert("Registration Success", "You can now login.");
    // }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    private void switchToRegister() {
        System.out.println("Switching to register screen...");
        // TODO: Implement actual scene change or pane toggle
    }

}
