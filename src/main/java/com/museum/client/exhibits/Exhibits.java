package com.museum.client.exhibits;

import com.museum.Actions;
import com.museum.client.DashboardController;
import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Exhibits {
    private ObservableList<Exhibit> exhibits;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public Exhibits() {
        getExhibits();
    }

    public void getExhibits() {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_EXHIBITS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Exhibit> receivedExhibits = (List<Exhibit>) in.readObject();
                exhibits = FXCollections.observableArrayList(receivedExhibits);
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

    public boolean addExhibit(Exhibit exhibit, byte[] imageData) {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.ADD_EXHIBIT);
            out.writeObject(exhibit);
            if (imageData != null) {
                out.writeObject(imageData);
            }
            in = new ObjectInputStream(socket.getInputStream());

            try {
                List<Exhibit> receivedExhibits = (List<Exhibit>) in.readObject();
                exhibits = FXCollections.observableArrayList(receivedExhibits);

                this.socket.close();
                return true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateExhibit(Exhibit exhibit, byte[] imageData) {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.UPDATE_EXHIBIT);
            out.writeObject(exhibit);
            out.writeObject(imageData);
            in = new ObjectInputStream(socket.getInputStream());

            try {
                List<Exhibit> receivedExhibits = (List<Exhibit>) in.readObject();
                exhibits = FXCollections.observableArrayList(receivedExhibits);

                this.socket.close();
                return true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Exhibit> getExhibitsList() {
        return exhibits;
    }

}
