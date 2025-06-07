package com.pekaboo.auth;

import com.pekaboo.App;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.UserRepository;
import com.pekaboo.repositories.postgres.PostgresUserRepository;
import com.pekaboo.util.Session;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;

public class AuthController {

    // Login
    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Button loginButton;

    // Register
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private TextField emailField;
    @FXML private ChoiceBox<String> userRoleBox;
    @FXML private DatePicker birthDatePicker;

    private final UserRepository userRepo = new PostgresUserRepository();

    @FXML
    public void initialize() {
        if (userRoleBox != null) {
            userRoleBox.getItems().addAll("PELANGGAN", "OPTOMETRIS");
        }
    }

    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        User user = userRepo.getUserByCredentials(username, password);
        if (user != null) {
            Session.setCurrentUser(user);
            showAlert("Success", "Login successful!");
            if(Session.getCurrentUser().getUserStatus().equals("PELANGGAN")){
                try {
                App.navigateToMain("/com/pekaboo/primary.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                } //kalau dashboard ada scenenya kalo gaada langsung ke page apa aja
            }
            else if (Session.getCurrentUser().getUserStatus().equals("OPTOMETRIS")){
                try {
                App.navigateToMain("/com/pekaboo/secondary.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                } //kalau dashboard ada scenenya kalo gaada langsung ke page apa aja
            }

        } else {
            showAlert("Login failed", "Username or password incorrect");
        }
    }

    @FXML
    private void handleRegister() {
        // check kalau username not null
        if (registerUsernameField == null || registerPasswordField == null || emailField == null || userRoleBox == null || birthDatePicker == null) {
            showAlert("Error", "Registration form not loaded properly.");
            return;
        }

        User newUser = new User();
        newUser.setUsername(registerUsernameField.getText());
        newUser.setPassword(registerPasswordField.getText()); // hash nanti (opsional)
        newUser.setEmail(emailField.getText());
        newUser.setTanggalLahir(birthDatePicker.getValue());
        newUser.setUserStatus(userRoleBox.getValue());

        userRepo.saveUser(newUser);
        showAlert("Registration Success", "You can now login.");

        // Navigate back ke login screen
        switchToLogin();
    }

    @FXML
    private void switchToRegister() {
        try {
            App.setRoot("auth/register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToLogin() {
        try {
            App.setRoot("auth/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
