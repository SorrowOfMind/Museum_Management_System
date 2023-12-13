package com.museum.client.tours;

import com.museum.Actions;
import com.museum.client.DashboardController;
import com.museum.models.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Tours <T> {
    private ObservableList<Tour> tours;
    private int insertedTourID = -1;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Tours() {
        getTours();
    }

    private void getTours() {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_TOURS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Tour> receivedTours = (List<Tour>) in.readObject();
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

    public ObservableList<Tour> getToursList(){
        return this.tours;
    }

    public int insertUpdateTour(Tour tour, boolean update){
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(update ? Actions.UPDATE_TOUR : Actions.INSERT_TOUR);
            out.writeObject(tour);
            in = new ObjectInputStream(socket.getInputStream());

            if(!update)
                insertedTourID = in.readInt();

            this.socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return insertedTourID;
    }

    public ObservableList<T> genericGetter(Actions action){
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
