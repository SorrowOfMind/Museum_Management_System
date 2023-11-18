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
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ExhibitsController implements Initializable {

    // EXHIBITS DATA
    private Exhibits exhibits;
    private ObservableList<Exhibit> exhibitsShowList;
    ObservableList<String> historicalPeriodsList = FXCollections.observableArrayList(
            "Starożytność", "Hellenizm", "Cesarski Rzym", "Średniowiecze", "Współczesność"
    );
    ObservableList<String> exhibitStatusList = FXCollections.observableArrayList(
            "wystawa", "magazyn", "konserwacja", "wypożyczony", "sprzedany"
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

    // UTILS
    private AlertMessage alert = new AlertMessage();

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
        if (exhibitName.getText().isEmpty()
                || exhibitAcquisitionDate.getValue() == null
                || exhibitValue.getText().isEmpty()
                || exhibitHistoricalPeriod.getSelectionModel().getSelectedItem() == null
                || exhibitLastConservation.getValue() == null
                || exhibitNextConservation.getValue() == null
                || exhibitStatus.getSelectionModel().getSelectedItem() == null
                || exhibitSecurity.getSelectionModel().getSelectedItem() == null
        ) {
            alert.info("Dodawanie eksponatu", "Proszę wypełnić wszystkie pola.");
        } else {
            String name = exhibitName.getText();
            String author = exhibitAuthor.getText();
            String creationDate = exhibitCreationDate.getText();
            String origins = exhibitOrigins.getText();
            String description = exhibitDescription.getText();
            Date acquisitionDate = Date.valueOf(exhibitAcquisitionDate.getValue());
            Integer value = Integer.valueOf(exhibitValue.getText());
            Integer ageID = Integer.valueOf(exhibitHistoricalPeriod.getSelectionModel().getSelectedIndex()) + 1;
            Date lastConservation = Date.valueOf(exhibitLastConservation.getValue());
            Date nextConservation = Date.valueOf(exhibitNextConservation.getValue());
            String status = String.valueOf(exhibitStatus.getSelectionModel().getSelectedItem());
            String security = String.valueOf(exhibitSecurity.getSelectionModel().getSelectedItem());

            System.out.println(name + " " + ageID + " " + lastConservation + " " + status);

            boolean isAdded = exhibits.addExhibit(new Exhibit(
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

            if (isAdded) {
                populateExhibitsTable();
                resetForm();
            }
        }
    }

    @FXML
    void clearExhibit(ActionEvent event) {
        resetForm();
    }

    @FXML
    void selectExhibit(MouseEvent event) {
        Exhibit selecteExhibit = exhibitsTable.getSelectionModel().getSelectedItem();
        int idx = exhibitsTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }

        exhibitIDText.setText(String.valueOf(selecteExhibit.getExhibitID()));
        exhibitName.setText(selecteExhibit.getName());
        exhibitAuthor.setText(selecteExhibit.getAuthor());
        exhibitCreationDate.setText(selecteExhibit.getCreationDate());
        exhibitOrigins.setText(selecteExhibit.getOrigins());
        exhibitDescription.setText(selecteExhibit.getDescription());
        exhibitAcquisitionDate.setValue(selecteExhibit.getAcquisitionDate().toLocalDate());
        exhibitValue.setText(String.valueOf(selecteExhibit.getValue()));
        exhibitHistoricalPeriod.getSelectionModel().select(selecteExhibit.getAgeID());
        exhibitLastConservation.setValue(selecteExhibit.getLastConservation().toLocalDate());
        exhibitNextConservation.setValue(selecteExhibit.getNextConservation().toLocalDate());
        exhibitStatus.getSelectionModel().select(selecteExhibit.getStatus());
        exhibitSecurity.getSelectionModel().select(selecteExhibit.getSecurity());
    }

    private void refreshExhibits() {
        exhibits.getExhibits();
        populateExhibitsTable();
    }

    private void resetForm() {
        exhibitName.setText("");
        exhibitAuthor.setText("");
        exhibitCreationDate.setText("");
        exhibitOrigins.setText("");
        exhibitDescription.setText("");
        exhibitAcquisitionDate.setValue(null);
        exhibitValue.setText("");
        exhibitHistoricalPeriod.getSelectionModel().clearSelection();
        exhibitLastConservation.setValue(null);
        exhibitNextConservation.setValue(null);
        exhibitStatus.getSelectionModel().clearSelection();
        exhibitSecurity.getSelectionModel().clearSelection();
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
