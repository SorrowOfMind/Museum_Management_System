package com.museum.client.overview;

import com.museum.client.DashboardController;
import com.museum.client.exhibitions.ExhibitionsController;
import com.museum.client.exhibits.ExhibitsController;
import com.museum.client.tours.ToursController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewController implements Initializable {
    @FXML
    private AnchorPane overviewView;

    private ExhibitsController exhibitsController;

    private ExhibitionsController exhibitionsController;

    private ToursController toursController;

    @FXML
    private Label exhibitsDueLabel;
    @FXML
    private Label exhibitsOverdueLabel;
    @FXML
    private Label exhibitionsLabel;

    @FXML
    private Label toursLabel;

    @FXML
    void showExhibitions(MouseEvent event) {
        exhibitionsController.showCurrentExhibitions();

        if (exhibitionsController.getCurrentExhibitionsListSize().equals("0")) return;

        DashboardController dashboardController = DashboardController.getInstance();
        dashboardController.setVisibleView(dashboardController.exhibitionsView, dashboardController.exhibitionsBtn);
    }

    @FXML
    void showExhibitsDue(MouseEvent event) {
        exhibitsController.showDueExhibits();

        if (exhibitsController.getExhibitsDueListSize().equals("0")) return;

        DashboardController dashboardController = DashboardController.getInstance();
        dashboardController.setVisibleView(dashboardController.exhibitsView, dashboardController.exhibitsBtn);
    }

    @FXML
    void showExhibitsOverdue(MouseEvent event) {
        exhibitsController.showOverdueExhibits();

        if (exhibitsController.getExhibitsOverdueListSize().equals("0")) return;

        DashboardController dashboardController = DashboardController.getInstance();
        dashboardController.setVisibleView(dashboardController.exhibitsView, dashboardController.exhibitsBtn);
    }

    @FXML
    void showTours(MouseEvent event) {
        toursController.showCurrentTours();

        if (toursController.getCurrentToursListSize().equals("0")) return;

        DashboardController dashboardController = DashboardController.getInstance();
        dashboardController.setVisibleView(dashboardController.toursView, dashboardController.toursButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitsController = ExhibitsController.getInstance();
        exhibitionsController = ExhibitionsController.getInstance();
        toursController = ToursController.getInstance();
        exhibitsDueLabel.setText(exhibitsController.getExhibitsDueListSize());
        exhibitsOverdueLabel.setText(exhibitsController.getExhibitsOverdueListSize());
        exhibitionsLabel.setText(exhibitionsController.getCurrentExhibitionsListSize());
        toursLabel.setText(toursController.getCurrentToursListSize());
    }
}
