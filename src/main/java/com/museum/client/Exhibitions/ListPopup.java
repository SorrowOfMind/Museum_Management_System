package com.museum.client.Exhibitions;

import com.museum.models.Exhibit;
import com.museum.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Callback;

@FunctionalInterface
interface ListPopUpReturn<T> {
    void getReturn(ObservableList<T> input);
}

public class ListPopup<T> {


    private Pane exhibitsPopup;

    private ListView<T> list;
    private Popup popup;

    private Button ok;
    private Window window;

    public class CustomCellFactory implements Callback<ListView<T>, ListCell<T>> {

        @Override
        public ListCell<T> call(ListView<T> param) {
            return new ListCell<T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else if (item instanceof Exhibit) {
                        Exhibit exhibit = (Exhibit)item;
                        setText(exhibit.getName());
                    }
                    else if (item instanceof Room){
                        Room room = (Room)item;
                        setText(String.format("Piętro: %s, Sala: %s, powierzchnia: %s", room.getFloor(), room.getRoomNumber(), room.getArea()));
                    }
                }
            };
        }


    }

    //TODO: NEED GENERIC LABEL
    public ListPopup(Pane popup, ListView<T> list, ObservableList<T> items, Button ok, Window window){

        this.list = list;
        this.list.setItems(items);
        this.list.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        this.list.setCellFactory(new CustomCellFactory());
        this.exhibitsPopup = popup;
        this.ok = ok;
        this.window = window;
    }

    public void DisplayPopUpAndGetResults(ListPopUpReturn<T> getter){
        this.popup = new Popup();
        popup.getContent().add(exhibitsPopup);
        exhibitsPopup.setVisible(true);

        popup.show(window);

        this.ok.setOnAction(e -> {

            ObservableList<T> selected = list.getSelectionModel().getSelectedItems();
            getter.getReturn(selected);
            exhibitsPopup.setVisible(false);
            this.popup = null;
        });

    }
}
