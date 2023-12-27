package com.museum.client.user;

import com.museum.Actions;
import com.museum.client.AlertMessage;
import com.museum.client.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private static SettingsController instance;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @FXML
    private AnchorPane settingsView;

    @FXML
    private Label agreementDateText;

    @FXML
    private Label agreementTypeText;

    @FXML
    private Label dateOfBirthText;

    @FXML
    private Label emailText;

    @FXML
    private Label fullNameText;

    @FXML
    private Label jobTitleText;

    @FXML
    private Label phoneText;

    @FXML
    private Label roleText;

    @FXML
    private PasswordField newPswd;
    @FXML
    private PasswordField oldPswd;
    @FXML
    private PasswordField repeatPswd;

    @FXML
    private Label usernameText;

    private User user = null;

    private AlertMessage alert = new AlertMessage();

    public void setUser(User user) {
        this.user = user;
        populateUserData();
    }

    private void populateUserData() {
        usernameText.setText(user.getUsername());
        fullNameText.setText(user.getUser().getName() + " " + user.getUser().getSurname());
        dateOfBirthText.setText(String.valueOf(user.getUser().getDateOfBirth()));
        phoneText.setText(user.getUser().getPhone());
        emailText.setText(user.getUser().getEmail());
        agreementDateText.setText(String.valueOf(user.getUser().getAgreementDate()));
        agreementTypeText.setText(user.getUser().getAgreementType());
        jobTitleText.setText(user.getUser().getJobTitle());
        roleText.setText(String.valueOf(user.getRole()));
    }

    @FXML
    void changePassword(ActionEvent event) {
        String oldPassword = oldPswd.getText();
        String newPassword = newPswd.getText();
        String repeatPassword = repeatPswd.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
            alert.info("Zmiana hasła", "Proszę wypełnić wszystkie pola.");
        } else if (!newPassword.equals(repeatPassword)) {
            alert.error("Zmiana hasła", "Nowe hasło i powtórzone hasło są różne.");
        } else {
            try {
                this.socket = new Socket(DashboardController.HOST, DashboardController.PORT);
                out = new ObjectOutputStream(this.socket.getOutputStream());
                out.writeObject(Actions.CHANGE_PSWD);
                out.writeObject(user.getUser().getWorkerID());
                out.writeObject(oldPassword);
                out.writeObject(newPassword);

                in = new ObjectInputStream(this.socket.getInputStream());
                boolean isChanged = in.readBoolean();

                if (isChanged) {
                    alert.info("Zmiana hasła", "Zmieniono hasło.");
                    oldPswd.setText("");
                    newPswd.setText("");
                    repeatPswd.setText("");
                } else {
                    alert.error("Zmiana hasła", "Stare hasło nie jest poprawne.");
                }

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public User getUser() {
        return user;
    }

    public static SettingsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }
}