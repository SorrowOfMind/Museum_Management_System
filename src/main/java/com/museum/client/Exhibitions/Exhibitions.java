package com.museum.client.Exhibitions;

import com.museum.Actions;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Exhibitions {
    private ObservableList<Exhibition> exhibitions;
    private int exhibitionsNumber;
    private int insertedExhibitionID;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private void getExhibitions() {
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_EXHIBITS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Exhibition> receivedExhibitions = (List<Exhibition>) in.readObject();
                exhibitions = FXCollections.observableArrayList(receivedExhibitions);
                exhibitionsNumber = exhibitions.size();

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

    public int insertExhibition(Exhibition exhibition){
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.INSERT_EXHIBITIONS);
            out.writeObject(exhibition);

            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("server " + exhibition);
            insertedExhibitionID = in.readInt();

            this.socket.close();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return insertedExhibitionID;
    }
}
