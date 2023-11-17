package com.museum.client.Exhibitions;
import com.museum.client.AlertMessage;
import com.museum.client.DatabaseHelper;
import com.museum.client.Exhibits;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import com.museum.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
    private Pane exhibitsPopup;
    @FXML
    private ListView<Exhibit> exhibitsList;

    @FXML
    private ChoiceBox<Object> exhibitionRoom;

    private  Popup popup;

    private ObservableList<Exhibit> selectedExhibits = FXCollections.observableArrayList();


    private AlertMessage alert = new AlertMessage();

    @FXML
    private Button popUpOK;

    public class CustomCellFactory implements Callback<ListView<Exhibit>, ListCell<Exhibit>> {

        @Override
        public ListCell<Exhibit> call(ListView<Exhibit> param) {
            return new ListCell<Exhibit>() {
                @Override
                protected void updateItem(Exhibit item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        }
    }

    @FXML
    private void showPopup() {
        if(this.popup != null) return;
        this.popup = new Popup();

        exhibitsList.setCellFactory(new CustomCellFactory());

        exhibitsList.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        ObservableList<Exhibit> exhibits = DatabaseHelper.getExhibits();

        exhibitsList.setItems(exhibits);

        popup.getContent().add(exhibitsPopup);
        exhibitsPopup.setVisible(true);

        popUpOK.setOnAction(e -> {
            System.out.println();
            this.selectedExhibits = exhibitsList.getSelectionModel().getSelectedItems();
            String exhibitsString = "";

            for(Exhibit x : selectedExhibits){
                exhibitsString = exhibitsString + x.getName() + ", ";
            }

            exhibitsInExhibitions.setText(exhibitsString);

            exhibitsPopup.setVisible(false);
            this.popup = null;
        });

        popup.show(exhibitsInExhibitions.getScene().getWindow());

    }

    private boolean validateExhibition(String title, LocalDate start, Integer room){

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

        if(room == null) {
            alert.error("Błąd danych", "Wystawa musi mieć początek");
            return false;
        }


        return true;
    }

    @FXML
    private void insertExhibition(ActionEvent e) {
        String title = this.exhibitionTitle.getText();
        LocalDate start = exhibitionStartDate.getValue();
        LocalDate endDate = exhibitionEndDate.getValue();


        if(!validateExhibition(title, start, 1)) return;

        ArrayList<Integer> exhibitIDs = new ArrayList<>();

        for(Exhibit exhibit : selectedExhibits){
            exhibitIDs.add(exhibit.getExhibitID());
        }

        final Exhibition exhibitionToSend = new Exhibition(title,
                Date.valueOf(start), endDate == null ? null : Date.valueOf(endDate), exhibitIDs, 1);
        Exhibitions exhibitions = new Exhibitions();
        System.out.println(exhibitions.insertExhibition(exhibitionToSend));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


         }

}