package com.museum.client;

import com.museum.models.Exhibit;
import javafx.collections.ObservableList;
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

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ExhibitsController implements Initializable {

    // EXHIBITS DATA
    private Exhibits exhibits;
    private ObservableList<Exhibit> exhibitsShowList;

    // EXHIBITS FORM
    @FXML
    private DatePicker exhibitAcquisitionDate;

    @FXML
    private Button exhibitAddBtn;

    @FXML
    private TextField exhibitAuthor;

    @FXML
    private TextField exhibitCreationDate;

    @FXML
    private TextArea exhibitDescription;

    @FXML
    private ComboBox<?> exhibitHistoricalPeriod;

    @FXML
    private Label exhibitIDText;

    @FXML
    private DatePicker exhibitLastConservation;

    @FXML
    private TextField exhibitName;

    @FXML
    private DatePicker exhibitNextConservation;

    @FXML
    private TextField exhibitOrigins;

    @FXML
    private Button exhibitResetBtn;

    @FXML
    private ComboBox<?> exhibitSecurity;

    @FXML
    private ComboBox<?> exhibitStatus;

    @FXML
    private Button exhibitUpdateBtn;

    @FXML
    private TextField exhibitValue;


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

    private void exhibitsShowList() {
        exhibitsShowList = exhibits.getExhibitsList();

        exhibitsTableID.setCellValueFactory(new PropertyValueFactory<>("exhibitID"));
        exhibitsTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        exhibitsTableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        exhibitsTableConservation.setCellValueFactory(new PropertyValueFactory<>("nextConservation"));
        exhibitsTableSecurity.setCellValueFactory(new PropertyValueFactory<>("security"));

        exhibitsTable.setItems(exhibitsShowList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibits = new Exhibits();
        exhibitsShowList();
    }
}
