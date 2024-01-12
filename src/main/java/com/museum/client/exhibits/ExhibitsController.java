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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class ExhibitsController implements Initializable {

    private static ExhibitsController instance;

    @FXML
    private AnchorPane exhibitsView;

    private Exhibits exhibits;
    private ObservableList<Exhibit> exhibitsShowList;
    private ObservableList<Exhibit> exhibitsDueList;
    private String exhibitsDueListSize;
    private String exhibitsOverdueListSize;
    private ObservableList<Exhibit> exhibitsOverdueList;

    private ObservableList<Exhibit> filteredExhibitsShowList = FXCollections.observableArrayList();
    ObservableList<String> historicalPeriodsList = FXCollections.observableArrayList(
            "Starożytność", "Hellenizm", "Cesarski Rzym", "Średniowiecze", "Nowożytność", "Epoka XIX w", "Współczesność"
    );
    ObservableList<String> exhibitStatusList = FXCollections.observableArrayList(
            "wystawa", "magazyn", "konserwacja", "wypożyczony", "sprzedany"
    );
    ObservableList<String> exhibitSecurityList = FXCollections.observableArrayList(
            "brak", "standard", "ekstra"
    );

    ObservableList<String> searchFiltersList = FXCollections.observableArrayList(
            "ID", "Nazwa", "Status"
    );
    private int activeFilter = 0;
    @FXML
    private ComboBox<String> searchFilters;

    @FXML
    private ImageView exhibitImage;
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

    @FXML
    private Button exhibitAddBtn;
    @FXML
    private Button exhibitResetBtn;
    @FXML
    private Button exhibitUpdateBtn;

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

    private AlertMessage alert = new AlertMessage();
    private Image image;
    private byte[] imageData = null;

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
    private void addExhibit(ActionEvent event) {
        Exhibit exhibit = getFormData();

        if (exhibit != null) {
            boolean isAdded = exhibits.addExhibit(exhibit, imageData);

            if (isAdded) {
                populateExhibitsTable();
                resetForm();
                imageData = null;
            }
        }
    }

    @FXML
    private void updateExhibit(ActionEvent event) {
        String exhibitID = exhibitIDText.getText();

        if (exhibitID.isEmpty()) {
            alert.info("Aktualizacja eksponatu", "Proszę wybrać eksponat.");
            return;
        }

        Exhibit exhibit = getFormData();

        if (exhibit != null) {
          boolean isUpdated = exhibits.updateExhibit(exhibit, imageData);

            if (isUpdated) {
                populateExhibitsTable();
                resetForm();
                imageData = null;
            }
        }
    }

    @FXML
    private void clearExhibit(ActionEvent event) {
        resetForm();
    }

    @FXML
    private void selectExhibit(MouseEvent event) {
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

        if (selectedExhibit.getFilePath() != null) {
            image = new Image(System.getProperty("user.dir") + selectedExhibit.getFilePath(), 160, 160, false, true);
            exhibitImage.setImage(image);
        }
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
            alert.info("Eksponat", "Proszę wypełnić wymagane pola.");
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
        filteredExhibitsShowList = FXCollections.observableArrayList();
        exhibits.getExhibits();
        populateExhibitsTable();
        resetForm();
    }

    private void resetForm() {
        exhibitIDText.setText("");
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

        exhibitImage.setImage(null);
        imageData = null;
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
                break;
        }

        populateExhibitsTable();
    }

    @FXML
    private void uploadImage() {
        FileChooser fileWindow = new FileChooser();
        fileWindow.setTitle("Wgraj zdjęcie");
        fileWindow.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File",  "*png"));

        File file = fileWindow.showOpenDialog(null);

        if (file != null) {
            try {
                image = new Image(file.toURI().toString(), 160, 160, false, true);
                exhibitImage.setImage(image);
                imageData = convertImageToByteArray(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private byte[] convertImageToByteArray(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void showDueExhibits() {
        filteredExhibitsShowList = exhibitsDueList;

        populateExhibitsTable();
    }

    public void showOverdueExhibits() {
        filteredExhibitsShowList = exhibitsOverdueList;

        populateExhibitsTable();
    }

    private void getExhibitsDue() {
        if (exhibitsShowList == null || exhibitsShowList.size() == 0) return;

        LocalDate currentDate = LocalDate.now();
        LocalDate updatedDate = currentDate.plusDays(30);
        exhibitsDueList = exhibitsShowList.filtered(exhibit -> exhibit.getNextConservation().toLocalDate().isBefore(updatedDate));
        exhibitsDueListSize = String.valueOf(exhibitsDueList.size());
    }

    private void getExhibitsOverdue() {
        if (exhibitsShowList == null || exhibitsShowList.size() == 0) return;

        LocalDate currentDate = LocalDate.now();

        exhibitsOverdueList = exhibitsShowList.filtered(exhibit -> exhibit.getNextConservation().toLocalDate().isBefore(currentDate));
        exhibitsOverdueListSize = String.valueOf(exhibitsOverdueList.size());
    }

    public String getExhibitsDueListSize() {
        return exhibitsDueListSize;
    }

    public String getExhibitsOverdueListSize() {
        return exhibitsOverdueListSize;
    }

    public static ExhibitsController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitHistoricalPeriod.setItems(historicalPeriodsList);
        exhibitStatus.setItems(exhibitStatusList);
        exhibitSecurity.setItems(exhibitSecurityList);
        searchFilters.setItems(searchFiltersList);
        searchFilters.setValue("ID");

        exhibits = new Exhibits();
        populateExhibitsTable();
        getExhibitsDue();
        getExhibitsOverdue();
        exhibitUpdateBtn.setDisable(true);

        exhibitsSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchExhibits(newValue);
            }
        });
        instance = this;
    }
}
