package com.museum.client.workers;

import com.museum.client.AlertMessage;
import com.museum.models.Worker;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class WorkersController implements Initializable {

    private Workers workers;

    private ObservableList<Worker> workersShowList;

    private ObservableList<Worker> filteredWorkersShowList = FXCollections.observableArrayList();

    ObservableList<String> agreementList = FXCollections.observableArrayList(
            "umowa o pracę", "umowa zlecenie", "B2B"
    );
    ObservableList<String> jobTitleList = FXCollections.observableArrayList(
            "kurator", "kustosz", "konserwator", "administracja", "technik", "obsługa"
    );

    ObservableList<String> searchFiltersList = FXCollections.observableArrayList(
            "ID", "Nazwisko", "Stanowisko"
    );
    private int activeFilter = 0;
    @FXML
    private ComboBox<String> searchFilters;


    @FXML
    private Label workerIDText;

    @FXML
    private TextField workerAccountNumber;

    @FXML
    private Button workerAddBtn;

    @FXML
    private DatePicker workerAgreementDate;

    @FXML
    private ComboBox<String> workerAgreementType;

    @FXML
    private DatePicker workerDateOfBirth;

    @FXML
    private TextField workerEmail;

    @FXML
    private ComboBox<String> workerJobTitle;

    @FXML
    private TextField workerName;

    @FXML
    private TextField workerPhone;

    @FXML
    private Button workerRefreshBtn;

    @FXML
    private Button workerResetBtn;

    @FXML
    private TextField workerSalary;

    @FXML
    private TextField workerSearch;

    @FXML
    private TextField workerSurname;

    @FXML
    private TableView<Worker> workerTable;


    @FXML
    private TableColumn<Worker, String> workerTableName;

    @FXML
    private TableColumn<Worker, String> workerTableEmail;

    @FXML
    private TableColumn<Worker, String> workerTableID;

    @FXML
    private TableColumn<Worker, String> workerTableJobTitle;

    @FXML
    private TableColumn<Worker, String> workerTablePhone;

    @FXML
    private TableColumn<Worker, String> workerTableSurname;

    @FXML
    private DatePicker workerTerminationDate;

    @FXML
    private Button workerUpdateBtn;

    @FXML
    private AnchorPane workersView;

    private AlertMessage alert = new AlertMessage();

    private void populateWorkersTable() {
        workersShowList = filteredWorkersShowList.size() > 0 ? filteredWorkersShowList : workers.getWorkersList();

        workerTableID.setCellValueFactory(new PropertyValueFactory<>("workerID"));
        workerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        workerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        workerTablePhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        workerTableEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        workerTableJobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        workerTable.setItems(workersShowList);
    }

    @FXML
    void addWorker(ActionEvent event) {
        Worker worker = getFormData();

        if (worker != null) {
            boolean isAdded = workers.addWorker(worker);

            if (isAdded) {
                populateWorkersTable();
                resetForm();
            }
        }
    }

    @FXML
    void clearWorker(ActionEvent event) {
        resetForm();
    }

    @FXML
    void refreshWorkers(ActionEvent event) {
        filteredWorkersShowList = FXCollections.observableArrayList();
        workers.getWorkers();
        populateWorkersTable();
        resetForm();
    }

    @FXML
    void selectWorker(MouseEvent event) {
        Worker selectedWorker = workerTable.getSelectionModel().getSelectedItem();

        int idx = workerTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }

        workerAddBtn.setDisable(true);
        workerUpdateBtn.setDisable(false);

        workerIDText.setText(String.valueOf(selectedWorker.getWorkerID()));
        workerName.setText(selectedWorker.getName());
        workerSurname.setText(selectedWorker.getSurname());
        workerDateOfBirth.setValue(selectedWorker.getDateOfBirth().toLocalDate());
        workerPhone.setText(selectedWorker.getPhone());
        workerEmail.setText(selectedWorker.getEmail());
        workerAgreementType.getSelectionModel().select(selectedWorker.getAgreementType());
        workerAgreementDate.setValue(selectedWorker.getAgreementDate().toLocalDate());
        workerAccountNumber.setText(selectedWorker.getAccountNumber());
        workerSalary.setText(String.valueOf(selectedWorker.getSalary()));
        workerJobTitle.getSelectionModel().select(selectedWorker.getJobTitle());

        if (selectedWorker.getTerminationDate() != null) {
            workerTerminationDate.setValue(selectedWorker.getTerminationDate().toLocalDate());
        }
    }

    private void resetForm() {
        workerIDText.setText("");
        workerName.setText("");
        workerSurname.setText("");
        workerDateOfBirth.setValue(null);
        workerPhone.setText("");
        workerEmail.setText("");
        workerTerminationDate.setValue(null);
        workerAgreementType.getSelectionModel().clearSelection();
        workerAgreementDate.setValue(null);
        workerAccountNumber.setText("");
        workerSalary.setText("");
        workerJobTitle.getSelectionModel().clearSelection();
    }

    @FXML
    void switchActiveFilter(ActionEvent event) {
        activeFilter = searchFilters.getSelectionModel().getSelectedIndex();
    }

    @FXML
    void updateWorker(ActionEvent event) {
        String workerID = workerIDText.getText();

        if (workerID.isEmpty()) {
            alert.info("Aktualizacja danych pracownika", "Proszę wybrać pracownika.");
            return;
        }

        Worker worker = getFormData();

        if (worker != null) {
            boolean isUpdated = workers.updateWorker(worker);

            if (isUpdated) {
                populateWorkersTable();
                resetForm();
            }
        }
    }

    private void searchWorkers(String searchText) {
        if (workersShowList == null || workersShowList.size() == 0) return;

        if (searchText.length() == 0) {
            filteredWorkersShowList = FXCollections.observableArrayList();
            populateWorkersTable();
            return;
        }

        switch (activeFilter) {
            case 0:
                filteredWorkersShowList = workersShowList.filtered(worker -> String.valueOf(worker.getWorkerID()).equals(searchText));
                break;
            case 1:
                filteredWorkersShowList = workersShowList.filtered(worker -> worker.getSurname().toLowerCase().contains(searchText.toLowerCase().trim()));
                break;
            case 2:
                filteredWorkersShowList = workersShowList.filtered(worker -> worker.getJobTitle().toLowerCase().contains(searchText.toLowerCase().trim()));
                break;
            default:
                break;
        }

        populateWorkersTable();
    }

    private Worker getFormData() {
        Worker worker = null;

        if (workerName.getText().isEmpty()
                || workerSurname.getText().isEmpty()
                || workerDateOfBirth.getValue() == null
                || workerPhone.getText().isEmpty()
                || workerEmail.getText().isEmpty()
                || workerAgreementType.getSelectionModel().getSelectedItem() == null
                || workerAgreementDate.getValue() == null
                || workerSalary.getText().isEmpty()
                || workerJobTitle.getSelectionModel().getSelectedItem() == null
                || workerAccountNumber.getText().isEmpty()
        ) {
            alert.info("Prasownik", "Proszę wypełnić wymagane pola.");
        } else {
            String name = workerName.getText();
            String surname = workerSurname.getText();
            Date dateOfBirth = Date.valueOf(workerDateOfBirth.getValue());
            Date terminationDate = workerTerminationDate.getValue() != null ? Date.valueOf(workerTerminationDate.getValue()) : null;
            String phone = workerPhone.getText();
            String email = workerEmail.getText();
            String agreementType = String.valueOf(workerAgreementType.getSelectionModel().getSelectedItem());
            Integer salary = Integer.valueOf(workerSalary.getText());
            String jobTitle = String.valueOf(workerJobTitle.getSelectionModel().getSelectedItem());
            String accountNumber = workerAccountNumber.getText();
            Date agreementDate = Date.valueOf(workerAgreementDate.getValue());

            if (workerIDText.getText().isEmpty()) {
                worker = new Worker(name, surname, dateOfBirth, phone, email, terminationDate, agreementType, agreementDate, accountNumber, salary, jobTitle);
            } else {
                worker = new Worker(Integer.valueOf(workerIDText.getText()), name, surname, dateOfBirth, phone, email, terminationDate, agreementType, agreementDate, accountNumber, salary, jobTitle);
            }

        }

        return worker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workerAgreementType.setItems(agreementList);
        workerJobTitle.setItems(jobTitleList);
        searchFilters.setItems(searchFiltersList);
        searchFilters.setValue("ID");

        workers = new Workers();
        populateWorkersTable();
        workerUpdateBtn.setDisable(true);

        workerSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchWorkers(newValue);
            }
        });
    }
}
