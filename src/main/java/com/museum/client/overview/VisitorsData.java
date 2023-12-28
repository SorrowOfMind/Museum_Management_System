package com.museum.client.overview;

import com.museum.Actions;
import com.museum.client.DashboardController;
import com.museum.models.Exhibit;
import com.museum.models.Visitor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class VisitorsData  {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public List<Visitor> getVisitorsData() {
        try {
            socket = new Socket(DashboardController.HOST, DashboardController.PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(Actions.GET_VISITORS);
            in = new ObjectInputStream(socket.getInputStream());
            List<Visitor> visitorsData = new ArrayList<>();
            try {
                visitorsData = (List<Visitor>) in.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.socket.close();
            return visitorsData;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
