package com.museum.client.exhibitions;
import com.museum.Actions;
import com.museum.utils.ListPopup;
import com.museum.client.AlertMessage;
import com.museum.client.exhibits.Exhibits;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import com.museum.models.Room;
import com.museum.models.Worker_Basic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExhibitionsController implements Initializable {
    private static ExhibitionsController instance;
    private ObservableList<Exhibition> exhibitionsList;
    private ObservableList<Exhibition> currentExhibitionsList;
    private String currentExhibitionsListSize;

    private ObservableList<Exhibition> filteredExhibitionsList = FXCollections.observableArrayList();

    // SEARCH FORM
    ObservableList<String> searchFiltersList = FXCollections.observableArrayList(
            "ID", "Nazwa"
    );
    private int activeFilter = 0;

    @FXML
    private ComboBox<String> searchExhibitionsFilters;

    @FXML
    private TextField exhibitionsSearch;
    @FXML
    private TableView<Exhibition> exhibitionsTable;
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

    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private  ObservableList<Worker_Basic> workers = FXCollections.observableArrayList();
    private ObservableList<Exhibit> exhibits = FXCollections.observableArrayList();
    private ObservableList<Room> selectedRooms= FXCollections.observableArrayList();;
    private  ObservableList<Worker_Basic> selectedWorkers = FXCollections.observableArrayList();
    private ObservableList<Exhibit> selectedExhibits = FXCollections.observableArrayList();

    private AlertMessage alert = new AlertMessage();

    @FXML
    private Button popUpOK;

    @FXML Label popupLabel;

    private ListPopup popup;

    private Exhibitions exhibitions;

    private Exhibition selectedExhibition;


    private void setSelectedExhibits(ObservableList<Exhibit> result){
            this.selectedExhibits.setAll(result);
            String exhibitsString = "";

            for(Exhibit x : this.selectedExhibits){
                exhibitsString = exhibitsString + x.getName() + ", ";
            }
            exhibitsInExhibitions.setText(exhibitsString);
            if(this.popup != null)
                this.popup = null;
    }

    private void setSelectedRooms(ObservableList<Room> result){

        this.selectedRooms.setAll(result);
        String roomString = "";

        for(Room x : this.selectedRooms){
            roomString = roomString + " piętro "+ x.getFloor() + " pokój " + x.getRoomNumber() +  ", ";
        }
        roomField.setText(roomString);
        if(this.popup != null)
            this.popup = null;
    }

    private void setSelectedWorkers(ObservableList<Worker_Basic> result){
        this.selectedWorkers.setAll(result);
        String workerString = "";

        for(Worker_Basic x : this.selectedWorkers){
            workerString = workerString + x.toString() + ", ";
        }
        workerField.setText(workerString);
        if(this.popup != null)
            this.popup = null;
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

        this.rooms.setAll(this.exhibitions.genericGetter(Actions.GET_ROOMS)) ;
        this.popupLabel.setText("Sale");
        this.popup = new ListPopup<>(popupPane, (ListView<Room>) popupList, rooms,  popUpOK, roomField.getScene().getWindow() );
        popup.DisplayPopUpAndGetResults(res -> this.setSelectedRooms(res));
    }

    @FXML
    private void showWorkerPopup() {
        if(this.popup != null) return;

        this.workers.setAll(this.exhibitions.genericGetter(Actions.GET_WORKERS_SIMPLIFIED));
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

    private void populateExhibitionsTable() {
        exhibitionsList = filteredExhibitionsList.size() > 0 ? filteredExhibitionsList : exhibitions.getExhibitionsList();
        exhibitionsTableID.setCellValueFactory(new PropertyValueFactory<>("exhibitionID"));
        exhibitionsTableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        exhibitionsTableStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        exhibitionsTableEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        exhibitionsTable.setItems(exhibitionsList);
    }

    private void populateFieldsForSelectedExhibition(Exhibition e){

        this.selectedExhibition = e;

        this.exhibitionAddBtn.setDisable(true);

        this.exhibitionIDText.setText("ID: " + selectedExhibition.getExhibitionID().toString());
        this.exhibitionTitle.setText(selectedExhibition.getTitle());

        this.exhibitionStartDate.setValue(this.selectedExhibition.getStartDate().toLocalDate());
        Date endDate = this.selectedExhibition.getEndDate();
        if(endDate != null)
            this.exhibitionEndDate.setValue(endDate.toLocalDate());

        ObservableList<Exhibit> filteredExhibits = FXCollections.observableArrayList(this.exhibits.stream()
                .filter(x ->  selectedExhibition.getExhibitIds().contains(x.getExhibitID())).collect(Collectors.toList()));
        setSelectedExhibits(filteredExhibits);

        ObservableList<Room> filteredRooms = FXCollections.observableArrayList(this.rooms.stream()
                .filter(x ->  selectedExhibition.getRoomIDs().contains(x.getID())).collect(Collectors.toList()));
        setSelectedRooms(filteredRooms);
        System.out.println("rooms " + filteredRooms.size() + " " + selectedExhibition.getRoomIDs().size());

        ObservableList<Worker_Basic> filteredWorkers =FXCollections.observableArrayList(this.workers.stream()
                .filter(x ->  selectedExhibition.getWorkerIDs().contains(x.getWorkerID())).collect(Collectors.toList()));
        setSelectedWorkers(filteredWorkers);

    }

    @FXML
    private void selectExhibition(){
        Exhibition selected = exhibitionsTable.getSelectionModel().getSelectedItem();
        int idx = exhibitionsTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }
        this.exhibitionUpdateBtn.setDisable(false);
        this.exhibitionAddBtn.setDisable(true);
        this.populateFieldsForSelectedExhibition(selected);

    }

    private Exhibition constructAndValidateData(){
        String title = this.exhibitionTitle.getText();
        LocalDate start = exhibitionStartDate.getValue();
        LocalDate endDate = exhibitionEndDate.getValue();


        if(!validateExhibition(title, start)) return null;

        ArrayList<Integer> exhibitIDs = new ArrayList<>();
        ArrayList<Integer> roomIDs = new ArrayList<>();
        ArrayList<Integer> workerIDs = new ArrayList<>();

        for(Exhibit exhibit : this.selectedExhibits){
            exhibitIDs.add(exhibit.getExhibitID());
        }

        for(Room room :  this.selectedRooms){
            roomIDs.add(room.getID());
        }
        System.out.println("roomes " + this.selectedRooms.size());
        for(Worker_Basic worker :  this.selectedWorkers){
            workerIDs.add(worker.getWorkerID());
        }
        System.out.println("workers " + this.selectedWorkers.size());
        return new Exhibition(title,
                Date.valueOf(start), endDate == null ? null : Date.valueOf(endDate), exhibitIDs, roomIDs, workerIDs);
    }

    @FXML
    private void insertExhibition() {
        Exhibition entity = constructAndValidateData();
        if(entity == null) return;

        boolean isInserted = exhibitions.insertUpdateExhibition(entity, false);

        if (isInserted) {
            populateExhibitionsTable();
            resetExhibition();
        }
    }

    @FXML
    private void updateExhibition(){
        final Exhibition entity = this.constructAndValidateData();
        entity.setExhibitionID(this.selectedExhibition.getExhibitionID());
        if(entity.equals(this.selectedExhibition)){
            this.alert.error("Błąd", "Nie wprowadzono żadnych zmian w wystawie.");
            return;
        }
        boolean isUpdated = exhibitions.insertUpdateExhibition(entity, true);

        if (isUpdated) {
            populateExhibitionsTable();
            resetExhibition();
        }
    }

    @FXML
    private void resetExhibition(){
        this.selectedExhibition = null;
        this.exhibitionUpdateBtn.setDisable(true);
        this.exhibitionAddBtn.setDisable(false);
        exhibitionIDText.setText("ID: ");
        exhibitionTitle.setText(null);
        exhibitionStartDate.setValue(null);
        exhibitionEndDate.setValue(null);
        exhibitsInExhibitions.setText(null);
        roomField.setText(null);
        workerField.setText(null);
    }

    @FXML
    private void refreshExhibitions(){
        this.refreshAll();
    }

    private void refreshAll(){
        Exhibits ex = new Exhibits();
        filteredExhibitionsList = FXCollections.observableArrayList();

        this.exhibits.setAll(ex.getExhibitsList());
        this.rooms.setAll(this.exhibitions.genericGetter(Actions.GET_ROOMS));
        this.workers.setAll(this.exhibitions.genericGetter(Actions.GET_WORKERS_SIMPLIFIED));
        populateExhibitionsTable();
    }

    private void searchExhibitions(String searchText) {
        if (exhibitionsList == null || exhibitionsList.size() == 0) return;

        if (searchText.length() == 0) {
            filteredExhibitionsList = FXCollections.observableArrayList();
            populateExhibitionsTable();
            return;
        }

        switch (activeFilter) {
            case 0:
                filteredExhibitionsList = exhibitionsList.filtered(exhibition -> String.valueOf(exhibition.getExhibitionID()).equals(searchText));
                break;
            case 1:
                filteredExhibitionsList = exhibitionsList.filtered(exhibition -> exhibition.getTitle().toLowerCase().contains(searchText.toLowerCase().trim()));
                break;
            default:
                break;
        }

        populateExhibitionsTable();
    }

    @FXML
    void switchActiveFilter(ActionEvent event) {
        activeFilter = searchExhibitionsFilters.getSelectionModel().getSelectedIndex();
    }

    private void getCurrentExhibitions() {
        if (exhibitionsList == null || exhibitionsList.size() == 0) return;

        LocalDate currentDate = LocalDate.now();

        currentExhibitionsList = exhibitionsList.filtered(exhibition -> exhibition.getStartDate().toLocalDate().isBefore(currentDate)
                && exhibition.getEndDate().toLocalDate().isAfter(currentDate));
        currentExhibitionsListSize = String.valueOf(currentExhibitionsList.size());
    }

    public void showCurrentExhibitions() {
        filteredExhibitionsList = currentExhibitionsList;

        populateExhibitionsTable();
    }

    public String getCurrentExhibitionsListSize() {
        return currentExhibitionsListSize;
    }

    public static ExhibitionsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchExhibitionsFilters.setItems(searchFiltersList);
        searchExhibitionsFilters.setValue("ID");
        this.exhibitions = new Exhibitions();
        refreshAll();
        getCurrentExhibitions();

        exhibitionsSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchExhibitions(newValue);
            }
        });

        instance = this;
    }

}