package com.museum.client;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class MuseumApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        new View(stage, "login-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }

}