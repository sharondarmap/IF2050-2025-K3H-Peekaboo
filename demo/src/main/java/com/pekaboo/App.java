package com.pekaboo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.pekaboo.auth.*;
import com.pekaboo.features.home.MainController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("auth/login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void navigateToMain(String homePageFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/pekaboo/home/main.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.loadPage(homePageFxml, null);

        scene.setRoot(root);
    }


    public static void main(String[] args) {
        launch();
    }

}