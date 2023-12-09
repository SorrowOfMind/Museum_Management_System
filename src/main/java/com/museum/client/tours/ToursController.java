package com.museum.client.tours;

import com.museum.Actions;
import com.museum.client.AlertMessage;
import com.museum.models.Tour;
import com.museum.models.Worker_Basic;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class ToursController implements Initializable {
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

    private AlertMessage alert = new AlertMessage();
    private Tours tours;
    private Tour selectedTour;
    private void populateToursTable() {
        toursList = this.tours.getToursList(this.toursSearch.getText());
        tableTourID.setCellValueFactory(new PropertyValueFactory<>("tourID"));
        tableGroupLeader.setCellValueFactory(new PropertyValueFactory<>("groupLeader"));
        tableTourDate.setCellValueFactory(new PropertyValueFactory<>("tourDate"));
        tableSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        toursTable.setItems(toursList);
    }

    private void populateFieldsForSelectedTour(Tour t){
        this.tourAddBtn.setDisable(true);
        this.selectedTour = t;
        this.tourIDText.setText("ID: " + selectedTour.getTourID().toString());
        this.tourLeader.setText(t.groupLeader);
        this.tourDate.setValue(t.getTourDate());
        this.tourHour.setText(t.getTourHour());
        this.tourStandard.setText(t.getStandardTicketCount().toString());
        this.tourDiscounted.setText(t.getDiscountTicketCount().toString());
        this.tourLanguage.setText(t.getLanguage());

        Optional<Worker_Basic> assigned = this.tourWorker.getItems().stream().filter(x -> x.getWorkerID() == t.getWorkerID()).findFirst();

        this.tourWorker.setValue(assigned.isPresent() ? assigned.get() : null);

        this.tourAddBtn.setDisable(true);
        this.tourResetBtn.setDisable(false);
        this.tourUpdateBtn.setDisable(false);
    }

    @FXML
    private void selectTour(){
        Tour selected = toursTable.getSelectionModel().getSelectedItem();
        int idx = toursTable.getSelectionModel().getSelectedIndex();

        if ((idx - 1) < -1) {
            return;
        }
        this.tourUpdateBtn.setDisable(false);
        this.tourAddBtn.setDisable(true);
        this.populateFieldsForSelectedTour(selected);
    }

    private Tour constructAndValidateData() {
        String leader = this.tourLeader.getText();
        if (leader.isEmpty() || leader == null) {
            alert.error("Błąd danych", "Wycieczka musi mieć lidera");
            return null;
        }

        LocalDate date = this.tourDate.getValue();
        if (date == null) {
            alert.error("Błąd danych", "Wycieczka musi mieć datę");
            return null;
        }

        String hour = this.tourHour.getText();
        String regex = "([0-1][0-9]|2[0-3]):([0-5][0-9])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(hour);

        if (hour.isEmpty() || !matcher.find()) {
            alert.error("Błąd danych","Wycieczka musi mieć poprawny format godziny");
            return null;
        }

        int normalTickets = parseInt(this.tourStandard.getText());
        int discountedTickets = parseInt(this.tourDiscounted.getText());
        if (normalTickets == 0 && discountedTickets == 0) {
            alert.error("Błąd danych", "Wycieczka musi mieć przynajmniej jeden sprzedany bilet");
            return null;
        }

        String language = this.tourLanguage.getText();
        Worker_Basic assignedWorker = this.tourWorker.getSelectionModel().getSelectedItem();

        return new Tour(leader, date, hour, (normalTickets + discountedTickets), language, assignedWorker != null ? assignedWorker.getWorkerID() : null, normalTickets, discountedTickets);
    }


    @FXML
    private void insertTour() {
        Tour entity = constructAndValidateData();
        if(entity == null) return;

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
        if(entity == null) return;

        if(entity.equals(this.selectedTour)){
            this.alert.error("Błąd", "Nie wprowadzono żadnych zmian w wycieczce.");
            return;
        }
        entity.setTourID(this.selectedTour.getTourID());
        tours.insertUpdateTour(entity, true);
        this.refreshAll();
    }

    @FXML
    private void resetTour(){
        this.selectedTour = null;
        this.tourUpdateBtn.setDisable(true);
        this.tourAddBtn.setDisable(false);
        tourIDText.setText("ID: ");
        tourLeader.setText(null);
        tourDate.setValue(null);
        tourHour.setText(null);
        tourStandard.setText("0");
        tourDiscounted.setText("0");
        tourLanguage.setText(null);
        tourWorker.setValue(null);
    }
    @FXML
    private void refreshTours(){
        this.refreshAll();
    }

    private void refreshAll(){
        populateToursTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tours = new Tours();
        refreshAll();

        tourStandard.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tourStandard.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tourDiscounted.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tourStandard.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        this.tourWorker.setItems(this.tours.genericGetter(Actions.GET_WORKERS_SIMPLIFIED));

    }
}
