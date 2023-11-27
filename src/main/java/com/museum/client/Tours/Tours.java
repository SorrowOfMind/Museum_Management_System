package com.museum.client.Tours;

import com.museum.Actions;
import com.museum.models.Exhibit;
import com.museum.models.Tour;  // Renamed from Exhibition
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Tours <T> {
    private ObservableList<Tour> tours;  // Renamed from exhibitions
    private int insertedTourID = -1;  // Renamed from insertedExhibitionID
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private void getTours(String filter) {  // Renamed from getExhibitions
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_TOURS);  // Renamed from GET_EXHIBITIONS
            out.writeObject(filter.isEmpty() ? "" : filter);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Tour> receivedTours = (List<Tour>) in.readObject();  // Renamed from receivedExhibitions
                tours = FXCollections.observableArrayList(receivedTours);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Tour> getToursList(String filter){  // Renamed from getExhibitionsList
        //getTours(filter);
        return this.tours;
    }

    public int insertUpdateTour(Tour tour, boolean update){  // Renamed from insertUpdateExhibition
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(update ? Actions.UPDATE_TOUR : Actions.INSERT_TOURS);  // Renamed from UPDATE_EXHIBITION and INSERT_EXHIBITIONS
            out.writeObject(tour);
            in = new ObjectInputStream(socket.getInputStream());

            if(!update)
                insertedTourID = in.readInt();  // Renamed from insertedExhibitionID

            this.socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return insertedTourID;
    }

    public ObservableList<T> genericGetter(Actions action){  // Unchanged
        ObservableList<T> res;
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(action);
            in = new ObjectInputStream(socket.getInputStream());
            List<T> receivedRes = (List<T>) in.readObject();
            res = FXCollections.observableArrayList(receivedRes);
            this.socket.close();
        } catch (UnknownHostException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
