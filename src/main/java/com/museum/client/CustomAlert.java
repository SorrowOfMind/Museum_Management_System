package com.museum.client;

import javafx.scene.control.Alert;

public class CustomAlert extends Alert {
    private Alert alert;

    public CustomAlert(AlertType alertType, String title, String contentText) {
        super(alertType);
        this.alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
