package com.pekaboo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pekaboo.auth.*;
import com.pekaboo.features.home.MainController;
import com.pekaboo.util.Session;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String lastMainPageFxml = "/com/pekaboo/features/home/main.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("auth/login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    //navigate to main
    public static void navigateToMain(String fxmlPath) throws IOException {
        boolean isOptometris = Session.getCurrentUser().getUserStatus().equals("OPTOMETRIS");

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/pekaboo/home/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        controller.loadNavbar(isOptometris);
        controller.loadPage(fxmlPath, null);

        scene.setRoot(root);
    }

    public static void setLastMainPage(String fxml) {
        lastMainPageFxml = fxml;
    }

    public static void backToLastMain() throws IOException {
        navigateToMain(lastMainPageFxml);
    }

    public static void main(String[] args) {
        launch();
    }

}