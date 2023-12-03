package com.museum.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // PORT
    public static final int PORT = 5000;
    public static final String HOST = "localhost";

    private User user;
    @FXML
    private Label usernameText;

    // DASHBOARD SIDE BUTTONS
    @FXML
    private Button overviewBtn;
    @FXML
    private Button exhibitsBtn;
    @FXML
    private Button exhibitionsBtn;
    @FXML
    private Button toursButton;

    // VIEWS
    @FXML
    private AnchorPane dashboardView;
    @FXML
    private AnchorPane overviewView;
    @FXML
    private AnchorPane exhibitsView;
    @FXML
    private AnchorPane exhibitionsView;
    @FXML
    private AnchorPane toursView;

    // CUSTOM
    private AnchorPane[] views;
    private Button[] menuButtons;
    private AlertMessage alert = new AlertMessage();

    private String activeBtnStyle = "-fx-border-color: #fff;";
    private String inactiveBtnStyle = "-fx-border-color: transparent;";

    public void setUser(User user) {
        this.user = user;
        usernameText.setText(user.getUsername());
    }

    @FXML
    protected void switchDashboardView(ActionEvent event) {
        if (event.getSource().equals(overviewBtn)) {
            setVisibleView(overviewView, overviewBtn);
        } else if (event.getSource().equals(exhibitsBtn)) {
            setVisibleView(exhibitsView, exhibitsBtn);
        }
        else if (event.getSource().equals(exhibitionsBtn)) {
            setVisibleView(exhibitionsView, exhibitionsBtn);
        }
        else if (event.getSource().equals(toursButton)) {
            setVisibleView(toursView, toursButton);
        }
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        if (alert.confirm("Potwierdzenie wylogowania","Czy na pewno chcesz się wylogować?" )) {
            dashboardView.getScene().getWindow().hide();
            this.user = null;
            new View("login-view.fxml");
        }
    }

    private void setVisibleView(AnchorPane visibleView, Button menuButton) {
        visibleView.setVisible(true);

        for (AnchorPane view : views) {
            if (view != visibleView) {
                view.setVisible(false);
            }
        }

        for (Button button : menuButtons) {
            if (button == menuButton) {
                button.setStyle(activeBtnStyle);
            } else {
                button.setStyle(inactiveBtnStyle);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView, exhibitionsView, toursView};
        menuButtons = new Button[]{overviewBtn, exhibitsBtn, exhibitionsBtn, toursButton};
        overviewBtn.setStyle(activeBtnStyle);
    }
}