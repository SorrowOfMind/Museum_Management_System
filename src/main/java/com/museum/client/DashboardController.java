package com.museum.client;

import com.museum.Actions;
import com.museum.models.Exhibit;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // DATA
    private Exhibits exhibits;
    private ObservableList<Exhibit> exhibitsShowList;

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
    @FXML
    private Button exhibitionsBtn;


    // EXHIBIT TABLE
    @FXML
    private TextField exhibitsSearch;
    @FXML
    private TableView<Exhibit> exhibitsTable;
    @FXML
    private TableColumn<Exhibit, String> exhibitsTableID;
    @FXML
    private TableColumn<Exhibit, String> exhibitsTableName;
    @FXML
    private TableColumn<Exhibit, String> exhibitsTableStatus;
    @FXML
    private TableColumn<?, ?> exhibitsTableConservation;
    @FXML
    private TableColumn<Exhibit, String> exhibitsTableSecurity;

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
    @FXML
    private AnchorPane exhibitionsView;

    private ExhibitionsController exhibitionsController = null;
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
        else if (event.getSource().equals(exhibitionsBtn)) {
            if(exhibitionsController == null) this.exhibitionsController = new ExhibitionsController();
            setVisibleView(exhibitionsView);
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

    private void exhibitsShowList() {
        exhibitsShowList = exhibits.getExhibitsList();
        for (Exhibit ex : exhibitsShowList) {
            System.out.println(ex.getExhibitID());
        }
        exhibitsTableID.setCellValueFactory(new PropertyValueFactory<>("exhibitID"));
        exhibitsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        exhibitsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        exhibitsTableSecurity.setCellValueFactory(new PropertyValueFactory<>("security"));

        exhibitsTable.setItems(exhibitsShowList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        views = new AnchorPane[]{overviewView, exhibitsView, exhibitionsView};
        exhibits = new Exhibits();
        exhibitsShowList();
    }
}