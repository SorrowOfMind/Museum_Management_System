package com.museum.client;
import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ExhibitionsController  {

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
    private ListView<String> exhibitsList;


    private AlertMessage alert = new AlertMessage();

    @FXML
    private Button popUpOK;

    @FXML
    private void showPopup() {
        Popup popup = new Popup();

        exhibitsList.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        ObservableList<String> list = FXCollections.observableArrayList();

        for(Exhibit x : DatabaseHelper.getExhibits()){
            list.add(x.getName());
        }

        exhibitsList.setItems(list);

        popup.getContent().add(exhibitsPopup);
        exhibitsPopup.setVisible(true);

        popUpOK.setOnAction(e -> {
            System.out.println();
            final ObservableList<String> selected = exhibitsList.getSelectionModel().getSelectedItems();
            String allItemsString = "";
            for(String item : selected){
                allItemsString = allItemsString  + item + ", ";
            }

            exhibitsInExhibitions.setText(allItemsString);

            exhibitsPopup.setVisible(false);
        });

        popup.show(exhibitsInExhibitions.getScene().getWindow());

    }

}