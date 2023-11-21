package com.museum.client.exhibits;

import com.museum.client.AlertMessage;
import com.museum.models.Exhibit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ResourceBundle;



public class ExhibitsController implements Initializable {

    // EXHIBITS DATA
    private Exhibits exhibits;

    private ObservableList<Exhibit> exhibitsShowList;
    private ObservableList<Exhibit> filteredExhibitsShowList;
    ObservableList<String> historicalPeriodsList = FXCollections.observableArrayList(
            "Starożytność", "Hellenizm", "Cesarski Rzym", "Średniowiecze", "Współczesność"
    );
    ObservableList<String> exhibitStatusList = FXCollections.observableArrayList(
            "wystawa", "magazyn", "konserwacja", "wypożyczony", "sprzedany"
    );
    ObservableList<String> exhibitSecurityList = FXCollections.observableArrayList(
            "brak", "standard", "ekstra"
    );

    // SEARCH FORM
    ObservableList<String> searchFiltersList = FXCollections.observableArrayList(
            "ID", "Nazwa", "Status"
    );
    private int activeFilter = 0;
    @FXML
    private ComboBox<String> searchFilters;

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

    // FORM BUTTONS
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
    private Button exhibitRefreshBtn;
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
        exhibitsShowList = filteredExhibitsShowList.size() > 0 ? filteredExhibitsShowList : exhibits.getExhibitsList();

        exhibitsTableID.setCellValueFactory(new PropertyValueFactory<>("exhibitID"));
        exhibitsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        exhibitsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        exhibitsTableConservation.setCellValueFactory(new PropertyValueFactory<>("nextConservation"));
        exhibitsTableSecurity.setCellValueFactory(new PropertyValueFactory<>("security"));

        exhibitsTable.setItems(exhibitsShowList);
    }

    @FXML
    void addExhibit(ActionEvent event) {
        Exhibit exhibit = getFormData();

        if (exhibit != null) {
            boolean isAdded = exhibits.addExhibit(exhibit);

            if (isAdded) {
                populateExhibitsTable();
                resetForm();
            }
        }
    }

    @FXML
    void updateExhibit(ActionEvent event) {
        String exhibitID = exhibitIDText.getText();

        if (exhibitID.isEmpty()) {
            alert.info("Aktualizacja eksponatu", "Proszę wybrać eksponat.");
            return;
        }

        Exhibit exhibit = getFormData();

        if (exhibit != null) {
          boolean isUpdated = exhibits.updateExhibit(exhibit);

            if (isUpdated) {
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
        Exhibit selectedExhibit = exhibitsTable.getSelectionModel().getSelectedItem();
        int idx = exhibitsTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }

        exhibitAddBtn.setDisable(true);
        exhibitUpdateBtn.setDisable(false);

        exhibitIDText.setText(String.valueOf(selectedExhibit.getExhibitID()));
        exhibitName.setText(selectedExhibit.getName());
        exhibitAuthor.setText(selectedExhibit.getAuthor());
        exhibitCreationDate.setText(selectedExhibit.getCreationDate());
        exhibitOrigins.setText(selectedExhibit.getOrigins());
        exhibitDescription.setText(selectedExhibit.getDescription());
        exhibitAcquisitionDate.setValue(selectedExhibit.getAcquisitionDate().toLocalDate());
        exhibitValue.setText(String.valueOf(selectedExhibit.getValue()));
        exhibitHistoricalPeriod.getSelectionModel().select(selectedExhibit.getAgeID() - 1);
        exhibitLastConservation.setValue(selectedExhibit.getLastConservation().toLocalDate());
        exhibitNextConservation.setValue(selectedExhibit.getNextConservation().toLocalDate());
        exhibitStatus.getSelectionModel().select(selectedExhibit.getStatus());
        exhibitSecurity.getSelectionModel().select(selectedExhibit.getSecurity());
    }

    private Exhibit getFormData() {
        Exhibit exhibit = null;

        if (exhibitName.getText().isEmpty()
                || exhibitAcquisitionDate.getValue() == null
                || exhibitValue.getText().isEmpty()
                || exhibitHistoricalPeriod.getSelectionModel().getSelectedItem() == null
                || exhibitLastConservation.getValue() == null
                || exhibitNextConservation.getValue() == null
                || exhibitStatus.getSelectionModel().getSelectedItem() == null
                || exhibitSecurity.getSelectionModel().getSelectedItem() == null
        ) {
            alert.info("Eksponat", "Proszę wypełnić wszystkie pola.");
        } else {
            Integer exhibitID = exhibitIDText.getText().isEmpty() ? 0 : Integer.valueOf(exhibitIDText.getText());
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

            exhibit = new Exhibit(
                    exhibitID,
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
            );
        }

        return exhibit;
    }

    @FXML
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

        exhibitAddBtn.setDisable(false);
        exhibitUpdateBtn.setDisable(true);
    }


    @FXML
    void switchActiveFilter(ActionEvent event) {
        activeFilter = searchFilters.getSelectionModel().getSelectedIndex();
    }

    private void searchExhibits(String searchText) {
        if (exhibitsShowList == null || exhibitsShowList.size() == 0) return;

        if (searchText.length() == 0) {
            filteredExhibitsShowList = FXCollections.observableArrayList();
            populateExhibitsTable();
            return;
        }

        switch (activeFilter) {
            case 0:
                filteredExhibitsShowList = exhibitsShowList.filtered(exhibit -> String.valueOf(exhibit.getExhibitID()).equals(searchText));
                break;
            case 1:
                filteredExhibitsShowList = exhibitsShowList.filtered(exhibit -> exhibit.getName().toLowerCase().contains(searchText.toLowerCase().trim()));
                break;
            case 2:
                filteredExhibitsShowList = exhibitsShowList.filtered(exhibit -> exhibit.getStatus().toLowerCase().contains(searchText.toLowerCase().trim()));
                break;
            default:
        }

        populateExhibitsTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitHistoricalPeriod.setItems(historicalPeriodsList);
        exhibitStatus.setItems(exhibitStatusList);
        exhibitSecurity.setItems(exhibitSecurityList);
        searchFilters.setItems(searchFiltersList);
        searchFilters.setValue("ID");
        filteredExhibitsShowList = FXCollections.observableArrayList();

        exhibits = new Exhibits();
        populateExhibitsTable();
        exhibitUpdateBtn.setDisable(true);

        exhibitsSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Call your search method with the entered text
                searchExhibits(newValue);
            }
        });
    }
}
