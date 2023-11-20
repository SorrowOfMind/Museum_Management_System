package com.museum.client.Exhibitions;
import com.museum.Actions;
import com.museum.client.AlertMessage;
import com.museum.client.exhibits.Exhibits;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import com.museum.models.Room;
import com.museum.models.Worker_Basic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ExhibitionsController implements Initializable {

    // SOCKET CONN
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;



    // EXHIBITIONS TABLE
    @FXML
    private TextField exhibitionsSearch;
    @FXML
    private TableView<?> exhibitionsTable;
    @FXML
    private TableColumn<?, ?> exhibitionsTableID;
    @FXML
    private TableColumn<?, ?> exhibitionsTableTitle;
    @FXML
    private TableColumn<?, ?> exhibitionsTableStartDate;
    @FXML
    private TableColumn<?, ?> exhibitionsTableEndDate;

    @FXML
    private Label exhibitionIDText;
    @FXML
    private TextField exhibitionTitle;
    @FXML
    private DatePicker exhibitionStartDate;
    @FXML
    private DatePicker exhibitionEndDate;

    @FXML
    private Button exhibitionResetBtn;
    @FXML
    private Button exhibitionAddBtn;
    @FXML
    private Button exhibitionUpdateBtn;
    @FXML
    private TextArea exhibitsInExhibitions;
    @FXML
    private Pane popupPane;

    @FXML
    private TextField roomField;
    @FXML
    private TextField workerField;
    @FXML
    private ListView<?> popupList;

    private ObservableList<Room> selectedRooms= FXCollections.observableArrayList();;

    private  ObservableList<Worker_Basic> selectedWorkers = FXCollections.observableArrayList();

    private ObservableList<Exhibit> selectedExhibits = FXCollections.observableArrayList();



    private AlertMessage alert = new AlertMessage();

    @FXML
    private Button popUpOK;

    @FXML Label popupLabel;

    private ListPopup popup;




    private void setSelectedExhibits(ObservableList<Exhibit> result){
            this.selectedExhibits.addAll(result);
            String exhibitsString = "";

            for(Exhibit x : this.selectedExhibits){
                exhibitsString = exhibitsString + x.getName() + ", ";
            }
            exhibitsInExhibitions.setText(exhibitsString);
            this.popup = null;
    }

    private void setSelectedRooms(ObservableList<Room> result){

        this.selectedRooms.addAll(result);
        String roomString = "";

        for(Room x : this.selectedRooms){
            roomString = roomString + " piętro "+ x.getFloor() + " pokój " + x.getRoomNumber() +  ", ";
        }
        roomField.setText(roomString); this.popup = null;
    }

    private void setSelectedWorkers(ObservableList<Worker_Basic> result){
        this.selectedWorkers.addAll(result);
        String workerString = "";

        for(Worker_Basic x : this.selectedWorkers){
            workerString = workerString + x.toString() + ", ";
        }
        workerField.setText(workerString); this.popup = null;
    }

    @FXML
    private void showExhibitsPopup() {
        if(this.popup != null) return;
        Exhibits ex = new Exhibits();
        ObservableList<Exhibit> exhibits = ex.getExhibitsList();
        this.popupLabel.setText("Eksponaty");
        this.popup = new ListPopup<>(popupPane, (ListView<Exhibit>) popupList, exhibits,  popUpOK, exhibitsInExhibitions.getScene().getWindow() );
        popup.DisplayPopUpAndGetResults(res -> this.setSelectedExhibits(res));



    }

    @FXML
    private void showRoomPopup() {
        if(this.popup != null) return;
        Exhibitions<Room> ex = new Exhibitions<>();

        ObservableList<Room> rooms = ex.genericGetter(Actions.GET_ROOMS);
        this.popupLabel.setText("Sale");
        this.popup = new ListPopup<>(popupPane, (ListView<Room>) popupList, rooms,  popUpOK, roomField.getScene().getWindow() );
        popup.DisplayPopUpAndGetResults(res -> this.setSelectedRooms(res));


    }

    @FXML
    private void showWorkerPopup() {
        if(this.popup != null) return;
        Exhibitions<Worker_Basic> ex = new Exhibitions<>();

        ObservableList<Worker_Basic> workers = ex.genericGetter(Actions.GET_WORKERS_FOR_EXHIBITION);
        this.popupLabel.setText("Pracownicy");
        this.popup = new ListPopup<>(popupPane, (ListView<Worker_Basic>) popupList, workers,  popUpOK, workerField.getScene().getWindow() );
        popup.DisplayPopUpAndGetResults(res -> this.setSelectedWorkers(res));


    }

    private boolean validateExhibition(String title, LocalDate start){

        if (title == null || title.isEmpty()) {
            alert.error("Błąd danych", "Wystawa musi mieć tytuł");
            return false;
        }
        if (selectedExhibits.isEmpty()) {
            alert.error("Błąd danych", "Wystawa musi mieć eksponaty");
            return false;
        }

        if(start == null) {
            alert.error("Błąd danych", "Wystawa musi mieć początek");
            return false;
        }

        if(this.selectedRooms.isEmpty()) {
            alert.error("Błąd danych", "Wystawa musi mieć miejsce");
            return false;
        }

        if(this.selectedWorkers.isEmpty()) {
            alert.error("Błąd danych", "Wystawa musi mieć opiekunów");
            return false;
        }



        return true;
    }

    @FXML
    private void insertExhibition(ActionEvent e) {
        String title = this.exhibitionTitle.getText();
        LocalDate start = exhibitionStartDate.getValue();
        LocalDate endDate = exhibitionEndDate.getValue();


       if(!validateExhibition(title, start)) return;

        ArrayList<Integer> exhibitIDs = new ArrayList<>();
        ArrayList<Integer> roomIDs = new ArrayList<>();
        ArrayList<Integer> workerIDs = new ArrayList<>();

        for(Exhibit exhibit : this.selectedExhibits){
            exhibitIDs.add(exhibit.getExhibitID());
        }

        for(Room room :  this.selectedRooms){
            roomIDs.add(room.getID());
        }

        for(Worker_Basic worker :  this.selectedWorkers){
            workerIDs.add(worker.getWorkerID());
        }

        final Exhibition exhibitionToSend = new Exhibition(title,
                Date.valueOf(start), endDate == null ? null : Date.valueOf(endDate), exhibitIDs, roomIDs, workerIDs);
        Exhibitions exhibitions = new Exhibitions();
        System.out.println(exhibitions.insertExhibition(exhibitionToSend));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}