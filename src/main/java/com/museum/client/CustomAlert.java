package com.museum.client;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CustomAlert extends Alert {
    private Alert alert;

    private AlertType alertType;

    private Optional<ButtonType> option;

    public CustomAlert(AlertType alertType, String title, String contentText) {
        super(alertType);
        this.alert = new Alert(alertType);
        this.alertType = alertType;
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        if (this.alertType == AlertType.CONFIRMATION) {
            this.option = alert.showAndWait();
        } else {
            alert.showAndWait();
        }
    }

    public Optional<ButtonType> getOption() {
        return this.option;
    }
}
