package com.museum.client;

import com.museum.client.MuseumApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    public static final String TITLE = "Archive";
    public static final String ICON_PATH = "images/icon.png";

    private FXMLLoader fxmlLoader;
    public View(Stage stage, String fxmlFile) throws IOException {
        fxmlLoader = new FXMLLoader(MuseumApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(getClass().getResource(ICON_PATH).toExternalForm()));
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public View(String fxmlFile) throws IOException {
        fxmlLoader = new FXMLLoader(MuseumApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(TITLE);
        stage.getIcons().add(new Image(getClass().getResource(ICON_PATH).toExternalForm()));
        stage.setScene(scene);
        stage.show();

    }

    public <T> T getFXMLController() {
        return this.fxmlLoader.getController();
    }
}
