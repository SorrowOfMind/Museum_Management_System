package com.museum.client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

// SECOND CLIENT ONLY FOR TESTING!!!!
public class MuseumApplication2 extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new View(stage, "login-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}