package com.museum.client.exhibitions;

import com.museum.Actions;
import com.museum.client.DashboardController;
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

public class Exhibitions <T> {
    private ObservableList<Exhibition> exhibitions;
    private int insertedExhibitionID= -1;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Exhibitions() {
        getExhibitions();
    }

    private void getExhibitions() {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_EXHIBITIONS);
            in = new ObjectInputStream(socket.getInputStream());
            try {
                List<Exhibition> receivedExhibitions = (List<Exhibition>) in.readObject();
                exhibitions = FXCollections.observableArrayList(receivedExhibitions);

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

    public ObservableList<Exhibition> getExhibitionsList(){
        return this.exhibitions;
    }

    public boolean insertUpdateExhibition(Exhibition exhibition, boolean update){
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(update ? Actions.UPDATE_EXHIBITION : Actions.INSERT_EXHIBITIONS);
            out.writeObject(exhibition);
            in = new ObjectInputStream(socket.getInputStream());

            try {
                List<Exhibition> receivedExhibitions = (List<Exhibition>) in.readObject();
                exhibitions = FXCollections.observableArrayList(receivedExhibitions);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.socket.close();
            return true;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
