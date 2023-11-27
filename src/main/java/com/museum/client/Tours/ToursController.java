package com.museum.client.Tours;

import com.museum.Actions;
import com.museum.Utils.ListPopup;
import com.museum.client.AlertMessage;
import com.museum.models.Tour;
import com.museum.models.Room;
import com.museum.models.Worker_Basic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ToursController implements Initializable {

    // SOCKET CONN
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ObservableList<Tour> toursList;

    @FXML
    private TextField toursSearch;
    @FXML
    private TableView<Tour> toursTable;
    @FXML
    private TableColumn<?, ?> tableTourID;
    @FXML
    private TableColumn<?, ?> tableGroupLeader;
    @FXML
    private TableColumn<?, ?> tableTourDate;
    @FXML
    private TableColumn<?, ?> tableSize;
    @FXML
    private TableColumn<?, ?> tableLanguage;
    @FXML
    private TableColumn<?, ?> tableWorkerFullName;


    @FXML
    private Label tourIDText;
    @FXML
    private TextField tourLeader;
    @FXML
    private DatePicker tourDate;
    @FXML
    private TextField tourHour;
    @FXML
    private TextField tourStandard;
    @FXML
    private TextField tourDiscounted;
    @FXML
    private TextField tourLanguage;

    @FXML
    private ComboBox<Worker_Basic> tourWorker;

    @FXML
    private Button tourResetBtn;
    @FXML
    private Button tourAddBtn;
    @FXML
    private Button tourUpdateBtn;

    @FXML
    private TextField workerField;

    private Worker_Basic selectedWorker;

    private AlertMessage alert = new AlertMessage();

    @FXML
    private Button popUpOK;

    @FXML Label popupLabel;

    private ListPopup popup;

    private Tours tours;

    private Tour selectedTour;
    private  ObservableList<Worker_Basic> workers = FXCollections.observableArrayList();
    @FXML
    private void setSelectedWorker(Worker_Basic worker){
       this.selectedWorker = worker;
    }

    private boolean validateTour(String title, Date start){

        if (title == null || title.isEmpty()) {
            alert.error("Błąd danych", "Wystawa musi mieć tytuł");
            return false;
        }

        if(start == null) {
            alert.error("Błąd danych", "Wystawa musi mieć początek");
            return false;
        }

        if(this.selectedWorker == null) {
            alert.error("Błąd danych", "Wystawa musi mieć opiekunów");
            return false;
        }

        return true;
    }

    private void populateToursTable() {
        //toursList = this.tours.getToursList(this.toursSearch.getText());
        tableTourID.setCellValueFactory(new PropertyValueFactory<>("tourID"));
        tableGroupLeader.setCellValueFactory(new PropertyValueFactory<>("groupLeader"));
        tableTourDate.setCellValueFactory(new PropertyValueFactory<>("tourDate"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        tableWorkerFullName.setCellValueFactory(new PropertyValueFactory<>("workerFullName"));


        toursTable.setItems(toursList);
    }

    private void populateFieldsForSelectedTour(Tour t){

        this.selectedTour = t;

        this.tourAddBtn.setDisable(true);

        this.tourIDText.setText("ID " + selectedTour.getTourID().toString());

        this.tourDate.setValue(this.selectedTour.getTourDate().toLocalDate());

    }

    @FXML
    private void selectTour(){
        Tour selected = toursTable.getSelectionModel().getSelectedItem();
        int idx = toursTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }

        this.populateFieldsForSelectedTour(selected);
    }

    private Tour constructAndValidateData(){
       return null;
    }

    @FXML
    private void insertTour() {
        Tour entity = constructAndValidateData();

        int id = tours.insertUpdateTour(entity, false);
        this.refreshAll();

        final Optional<Tour> t = this.toursList.stream().filter(x -> x.getTourID() == id).findFirst();
        if(t.isPresent()){
            populateFieldsForSelectedTour(t.get());
        }
        this.refreshTours();
    }

    @FXML
    private void updateTour(){
        final Tour entity = this.constructAndValidateData();

        if(entity.equals(this.selectedTour)){
            this.alert.error("Błąd", "Nie wprowadzono żadnych zmian w wystawie.");
            return;
        }
        System.out.println(this.selectedTour.getTourID());
        tours.insertUpdateTour(entity, true);
        this.refreshTours();
    }

    @FXML
    private void refreshTours(){
        this.refreshAll();
    }

    private void refreshAll(){
        populateToursTable();
        this.workers.setAll(this.tours.genericGetter(Actions.GET_WORKERS_SIMPLIFIED));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tours = new Tours();
        refreshAll();
    }
}
