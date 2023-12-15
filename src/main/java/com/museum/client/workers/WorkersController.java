package com.museum.client.workers;

import com.museum.client.exhibits.Exhibits;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkersController implements Initializable {

    ObservableList<String> agreementList = FXCollections.observableArrayList(
            "umowa o pracę", "umowa zlecenie", "B2B"
    );
    ObservableList<String> jobTitleList = FXCollections.observableArrayList(
            "kurator", "kustosz", "konserwator", "administracja", "technik", "obsługa"
    );

    @FXML
    private TableColumn<?, ?> TableName;

    @FXML
    private Label exhibitIDText;

    @FXML
    private ComboBox<String> searchFilters;

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
    private TableView<?> workerTable;

    @FXML
    private TableColumn<?, ?> workerTableEmail;

    @FXML
    private TableColumn<?, ?> workerTableID;

    @FXML
    private TableColumn<?, ?> workerTableJobTitle;

    @FXML
    private TableColumn<?, ?> workerTablePhone;

    @FXML
    private TableColumn<?, ?> workerTableSurname;

    @FXML
    private DatePicker workerTerminationDate;

    @FXML
    private Button workerUpdateBtn;

    @FXML
    private AnchorPane workersView;

    @FXML
    void addWorker(ActionEvent event) {

    }

    @FXML
    void clearWorker(ActionEvent event) {

    }

    @FXML
    void refreshWorkers(ActionEvent event) {

    }

    @FXML
    void selectExhibit(MouseEvent event) {

    }

    @FXML
    void switchActiveFilter(ActionEvent event) {

    }

    @FXML
    void updateWorker(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workerAgreementType.setItems(agreementList);
        workerJobTitle.setItems(jobTitleList);

//        exhibitsSearch.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                searchExhibits(newValue);
//            }
//        });
    }

}
