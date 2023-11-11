package com.museum.client;

import com.museum.Actions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;

public class LoginController {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField usernameText;

    private AlertMessage alert = new AlertMessage();

    @FXML
    protected void login(ActionEvent event) throws IOException {
        try {
            String username = usernameText.getText();
            String password = passwordText.getText();
            System.out.println(usernameText.getText() + " " + passwordText.getText());

            if (username.isEmpty() || password.isEmpty()) {
                alert.info("Logowanie", "Proszę wypełnić wszystkie pola.");
            } else {
                this.socket = new Socket("localhost", 5000);
                out = new ObjectOutputStream(this.socket.getOutputStream());
                out.writeObject(Actions.LOGIN);
                out.writeObject(username);
                out.writeObject(password);

                in = new ObjectInputStream(this.socket.getInputStream());
                boolean isAuthenticated = in.readBoolean();
                socket.close();

                if (isAuthenticated) {
                    User user = new User(username); // populate with more data: role, department etc
                    loginForm.getScene().getWindow().hide();
                    View dashboardView = new View("dashboard.fxml");
                    DashboardController dashboardController = dashboardView.getFXMLController();
                    dashboardController.setUser(user);
                    dashboardController.setSocket(this.socket);
                } else {
                    alert.error("Błąd logowania", "Nazwa użytkownika lub hasło są niepoprawne.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}