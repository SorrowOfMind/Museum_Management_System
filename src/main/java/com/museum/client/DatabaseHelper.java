package com.museum.client;

import com.museum.Actions;
import com.museum.models.Exhibit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class DatabaseHelper {
    public static ObservableList<Exhibit> getExhibits() {
        ObservableList<Exhibit> exhibits;
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_EXHIBITS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                Object object = in.readObject();
                System.out.println("client");
                exhibits = FXCollections.observableList((List<Exhibit>)object);
                for (Exhibit ex : exhibits) {
                    System.out.println(ex.exhibitID + " " + ex.name + " " + ex.status);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exhibits;


    }

}
