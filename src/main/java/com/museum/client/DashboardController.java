package com.museum.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Optional;

public class DashboardController {
    private User user;
    @FXML
    private Label usernameText;

    @FXML
    private AnchorPane dashboardView;

    public void setUser(User user) {
        this.user = user;
        usernameText.setText(user.getUsername());
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Logout confirmation", "Are you sure you want to logout?");
        Optional<ButtonType> option = alert.getOption();

        if (option.get().equals(ButtonType.OK)) {
            dashboardView.getScene().getWindow().hide();
            this.user = null;
            new View("login-view.fxml");
        }
    }
}
