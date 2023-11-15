package com.museum.client;

import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ExhibitsController implements Initializable {

    // EXHIBITS DATA
    private Exhibits exhibits;
    private ObservableList<Exhibit> exhibitsShowList;
    ObservableList<String> historicalPeriodsList = FXCollections.observableArrayList(
            "Starożytność", "Hellenizm", "Cesarski Rzym", "Średniowiecze", "Współczesność"
    );
    ObservableList<String> exhibitStatusList = FXCollections.observableArrayList(
            "Wystawa", "Magazyn", "Konserwacja", "Wypożyczony", "Sprzedany"
    );
    ObservableList<String> exhibitSecurityList = FXCollections.observableArrayList(
            "brak", "standard", "ekstra"
    );


    // EXHIBITS FORM
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
    private TextField exhibitValue;
    @FXML
    private ComboBox<String> exhibitHistoricalPeriod;
    @FXML
    private DatePicker exhibitLastConservation;
    @FXML
    private DatePicker exhibitNextConservation;
    @FXML
    private ComboBox<String> exhibitStatus;
    @FXML
    private ComboBox<String> exhibitSecurity;

    @FXML
    private Button exhibitAddBtn;
    @FXML
    private Button exhibitResetBtn;
    @FXML
    private Button exhibitUpdateBtn;

    // EXHIBITS TABLE
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
    private TableColumn<Exhibit, Date> exhibitsTableConservation;
    @FXML
    private TableColumn<Exhibit, String> exhibitsTableSecurity;

    private void populateExhibitsTable() {
        exhibitsShowList = exhibits.getExhibitsList();

        exhibitsTableID.setCellValueFactory(new PropertyValueFactory<>("exhibitID"));
        exhibitsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        exhibitsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        exhibitsTableConservation.setCellValueFactory(new PropertyValueFactory<>("nextConservation"));
        exhibitsTableSecurity.setCellValueFactory(new PropertyValueFactory<>("security"));

        exhibitsTable.setItems(exhibitsShowList);
    }

    @FXML
    void addExhibit(ActionEvent event) {
        // TODO: check if valid
        String name = exhibitName.getText();
        String author = exhibitAuthor.getText();
        String creationDate = exhibitCreationDate.getText();
        String origins = exhibitOrigins.getText();
        String description = exhibitDescription.getText();
        Date acquisitionDate = Date.valueOf(exhibitAcquisitionDate.getValue());
        Integer value = Integer.valueOf(exhibitValue.getText());
        Integer ageID = Integer.valueOf(exhibitHistoricalPeriod.getSelectionModel().getSelectedIndex());
        Date lastConservation = Date.valueOf(exhibitLastConservation.getValue());
        Date nextConservation = Date.valueOf(exhibitNextConservation.getValue());
        String status = String.valueOf(exhibitHistoricalPeriod.getSelectionModel().getSelectedItem());
        String security = String.valueOf(exhibitHistoricalPeriod.getSelectionModel().getSelectedItem());

        exhibits.addExhibit(new Exhibit(
                0,
                name,
                author,
                creationDate,
                origins,
                description,
                acquisitionDate,
                value,
                ageID,
                lastConservation,
                nextConservation,
                status,
                security
        ));
    }

    private void refreshExhibits() {
        exhibits.getExhibits();
        populateExhibitsTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitHistoricalPeriod.setItems(historicalPeriodsList);
        exhibitStatus.setItems(exhibitStatusList);
        exhibitSecurity.setItems(exhibitSecurityList);
        exhibits = new Exhibits();
        populateExhibitsTable();
    }
}
