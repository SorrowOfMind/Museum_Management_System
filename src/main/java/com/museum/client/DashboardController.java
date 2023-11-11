package com.museum.client;

import com.museum.DataModels.Age;
import com.museum.Enums.DatabaseQuery;
import com.museum.Enums.Table;
import jakarta.persistence.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private User user;
    private Socket socket;

    private DatabaseHelper databaseHelper;
    @FXML
    private Label usernameText;

    // DASHBOARD SIDE BUTTONS
    @FXML
    private Button overviewBtn;
    @FXML
    private Button exhibitsBtn;
    @FXML
    private Button exhibitionsBtn;


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
    private AnchorPane exhibitionsView;
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

    public void setSocket(Socket socket){
        this.socket = socket;
        this.databaseHelper = new DatabaseHelper(socket);
    }

    private void handleExhibitsSelected(){
        setVisibleView(exhibitsView);



    }

    @FXML
    protected void switchDashboardView(ActionEvent event) {
        if (event.getSource().equals(overviewBtn)) {
            setVisibleView(overviewView);
        } else if (event.getSource().equals(exhibitsBtn)) {
            handleExhibitsSelected();
        }
        else if (event.getSource().equals(exhibitionsBtn)) {
            setVisibleView(exhibitionsView);
        }
        else if (event.getSource().equals(exhibitionsBtn)) {
            final QueryHelper helper = new QueryHelper();
            final Age age = new Age(1, null, null, null);
            try{
                System.out.println(this.databaseHelper.ExecuteDatabaseQuery(helper.ConstructQuery(DatabaseQuery.Select, Table.Age, age)));
            }catch(ClassNotFoundException | IOException e){
                e.printStackTrace();
            }

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
                System.out.println("set it false");
                view.setVisible(false);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView, exhibitionsView};
    }
}
