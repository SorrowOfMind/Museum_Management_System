package com.museum.client;

import com.museum.Actions;
import com.museum.models.Exhibit;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // SOCKET CONN
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private User user;
    @FXML
    private Label usernameText;

    // DASHBOARD SIDE BUTTONS
    @FXML
    private Button overviewBtn;
    @FXML
    private Button exhibitsBtn;


    // EXHIBIT TABLE
    @FXML
    private TextField exhibitsSearch;
    @FXML
    private TableView<?> exhibitsTable;
    @FXML
    private TableColumn<?, ?> exhibitsTableID;
    @FXML
    private TableColumn<?, ?> exhibitsTableName;
    @FXML
    private TableColumn<?, ?> exhibitsTableStatus;
    @FXML
    private TableColumn<?, ?> exhibitsTableConservation;
    @FXML
    private TableColumn<?, ?> exhibitsTableSecurity;

    // EXHIBIT FORM
    @FXML
    private Label exhibitIDText;
    @FXML
    private TextField exhibitName;
    @FXML
    private TextField exhibitAuthor;
    @FXML
    private TextField exhibitCreationDate;
    @FXML
    private TextField exhibitOrigins;
    @FXML
    private TextArea exhibitDescription;
    @FXML
    private DatePicker exhibitAcquisitionDate;
    @FXML
    private TextField exhibitWorth;
    @FXML
    private ComboBox<?> exhibitHistoricalPeriod;
    @FXML
    private DatePicker exhibitLastConservation;
    @FXML
    private DatePicker exhibitNextConservation;
    @FXML
    private ComboBox<?> exhibitStatus;
    @FXML
    private ComboBox<?> exhibitSecurity;
    @FXML
    private Button exhibitResetBtn;
    @FXML
    private Button exhibitAddBtn;
    @FXML
    private Button exhibitUpdateBtn;

    // VIEWS
    @FXML
    private AnchorPane exhibitsView;
    @FXML
    private AnchorPane overviewView;
    @FXML
    private AnchorPane dashboardView;
    private AnchorPane[] views;
    private AlertMessage alert = new AlertMessage();

    public void setUser(User user) {
        this.user = user;
        usernameText.setText(user.getUsername());
    }

    @FXML
    protected void switchDashboardView(ActionEvent event) {
        if (event.getSource().equals(overviewBtn)) {
            setVisibleView(overviewView);
        } else if (event.getSource().equals(exhibitsBtn)) {
            setVisibleView(exhibitsView);
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

    private void setVisibleView(AnchorPane visibleView) {
        visibleView.setVisible(true);
        for (AnchorPane view : views) {
            if (view != visibleView) {
                view.setVisible(false);
            }
        }
    }

    private void getExhibits() {
        List<Exhibit> exhibits = new ArrayList<>();
        try {
            this.socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(this.socket.getOutputStream());
            out.writeObject(Actions.GET_EXHIBITS);
            in = new ObjectInputStream(this.socket.getInputStream());
            try {
                Object object = in.readObject();
                System.out.println("client");
                exhibits = (List<Exhibit>)object;
                for (Exhibit ex : exhibits) {
                    System.out.println(ex.id + " " + ex.name + " " + ex.status);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            this.socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView};
        getExhibits();
    }
}