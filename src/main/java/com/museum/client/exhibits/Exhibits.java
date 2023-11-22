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
    private int exhibitsNumber;
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
                exhibitsNumber = exhibits.size();

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

    public boolean addExhibit(Exhibit exhibit) {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.ADD_EXHIBIT);
            out.writeObject(exhibit);
            in = new ObjectInputStream(socket.getInputStream());

            try {
                List<Exhibit> receivedExhibits = (List<Exhibit>) in.readObject();
                exhibits = FXCollections.observableArrayList(receivedExhibits);
                exhibitsNumber = exhibits.size();

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

    public boolean updateExhibit(Exhibit exhibit) {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.UPDATE_EXHIBIT);
            out.writeObject(exhibit);
            in = new ObjectInputStream(socket.getInputStream());

            try {
                List<Exhibit> receivedExhibits = (List<Exhibit>) in.readObject();
                exhibits = FXCollections.observableArrayList(receivedExhibits);
                exhibitsNumber = exhibits.size();

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

    public int getExhibitsNumber() {
        return exhibitsNumber;
    }
}