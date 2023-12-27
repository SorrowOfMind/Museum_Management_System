package com.museum.client;

import com.museum.client.user.Roles;
import com.museum.client.user.SettingsController;
import com.museum.client.user.User;
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
    @FXML
    public Button workersBtn;
    @FXML
    public Button settingsBtn;

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
    @FXML
    public AnchorPane workersView;
    @FXML
    public AnchorPane settingsView;

    private SettingsController settingsController;

    // CUSTOM
    private AnchorPane[] views;
    private Button[] menuButtons;
    private AlertMessage alert = new AlertMessage();

    private String activeBtnStyle = "-fx-border-color: #fff;";
    private String inactiveBtnStyle = "-fx-border-color: transparent;";

    public void setUser(User user) {
        this.user = user;
        usernameText.setText(user.getUsername());
        if (user.getRole() != Roles.SUPERADMIN.ordinal() && user.getRole() != Roles.ADMIN.ordinal()) {
            workersView.setVisible(false);
            workersBtn.setVisible(false);
        }
    }

    @FXML
    public void switchDashboardView(ActionEvent event) {
        if (event.getSource().equals(overviewBtn)) {
            setVisibleView(overviewView, overviewBtn);
        } else if (event.getSource().equals(exhibitsBtn)) {
            setVisibleView(exhibitsView, exhibitsBtn);
        } else if (event.getSource().equals(exhibitionsBtn)) {
            setVisibleView(exhibitionsView, exhibitionsBtn);
        } else if (event.getSource().equals(toursButton)) {
            setVisibleView(toursView, toursButton);
        } else if (event.getSource().equals(workersBtn)) {
            setVisibleView(workersView, workersBtn);
        } else if (event.getSource().equals(settingsBtn)) {
            if (settingsController.getUser() == null) {
                settingsController.setUser(user);
            }
            setVisibleView(settingsView);
        }
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
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

    public void setVisibleView(AnchorPane visibleView) {
        visibleView.setVisible(true);

        for (AnchorPane view : views) {
            if (view != visibleView) {
                view.setVisible(false);
            }
        }

        for (Button button : menuButtons) {
            button.setStyle(inactiveBtnStyle);
        }
    }

    public static DashboardController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView, exhibitionsView, toursView, workersView, settingsView};
        menuButtons = new Button[]{overviewBtn, exhibitsBtn, exhibitionsBtn, toursButton, workersBtn, settingsBtn};
        overviewBtn.setStyle(activeBtnStyle);

        settingsController = SettingsController.getInstance();

        instance = this;
    }
}