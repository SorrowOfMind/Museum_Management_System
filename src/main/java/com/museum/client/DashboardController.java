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

    private static DashboardController instance;

    // PORT
    public static final int PORT = 5000;
    public static final String HOST = "localhost";

    private User user;
    @FXML
    private Label usernameText;

    // DASHBOARD SIDE BUTTONS
    @FXML
    public Button overviewBtn;
    @FXML
    public Button exhibitsBtn;
    @FXML
    public Button exhibitionsBtn;
    @FXML
    public Button toursButton;

    // VIEWS
    @FXML
    public AnchorPane dashboardView;
    @FXML
    public AnchorPane overviewView;
    @FXML
    public AnchorPane exhibitsView;
    @FXML
    public AnchorPane exhibitionsView;
    @FXML
    public AnchorPane toursView;

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
    public void switchDashboardView(ActionEvent event) {
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

    public void setVisibleView(AnchorPane visibleView, Button menuButton) {
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

    public static DashboardController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView, exhibitionsView, toursView};
        menuButtons = new Button[]{overviewBtn, exhibitsBtn, exhibitionsBtn, toursButton};
        overviewBtn.setStyle(activeBtnStyle);
        instance = this;
    }
}