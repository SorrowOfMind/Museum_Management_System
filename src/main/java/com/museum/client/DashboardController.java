package com.museum.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    private User user;
    @FXML
    private Label usernameText;

    public void setUser(User user) {
        this.user = user;
        usernameText.setText(user.getUsername());
    }
}
