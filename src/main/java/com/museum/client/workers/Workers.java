package com.museum.client.workers;

import com.museum.Actions;
import com.museum.client.DashboardController;
import com.museum.models.Exhibit;
import com.museum.models.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Workers {
    private ObservableList<Worker> workers;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Workers() {
        getWorkers();
    }

    public void getWorkers() {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_WORKERS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Worker> receivedExhibits = (List<Worker>) in.readObject();
                workers = FXCollections.observableArrayList(receivedExhibits);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
