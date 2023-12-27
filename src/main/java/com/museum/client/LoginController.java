package com.museum.client;

import com.museum.Actions;
import com.museum.client.user.User;
import com.museum.models.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    protected void login() throws IOException {
        try {
            String username = usernameText.getText();
            String password = passwordText.getText();

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

                if (isAuthenticated) {
                    int role = in.readInt();
                    Worker worker = (Worker) in.readObject();
                    User user = new User(username, role, worker);
                    loginForm.getScene().getWindow().hide();
                    View dashboardView = new View("dashboard.fxml");
                    DashboardController dashboardController = dashboardView.getFXMLController();
                    dashboardController.setUser(user);
                } else {
                    alert.error("Błąd logowania", "Nazwa użytkownika lub hasło są niepoprawne.");
                }

                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onEnter(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER){
            try{
                this.login();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

}