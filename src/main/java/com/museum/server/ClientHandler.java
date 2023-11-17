package com.museum.server;

import com.museum.Actions;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new ObjectInputStream(this.clientSocket.getInputStream());
        out = new ObjectOutputStream(this.clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            Actions action = (Actions) in.readObject();

            switch(action) {
                case LOGIN:
                    String username = (String) in.readObject();
                    String password = (String) in.readObject();

                    AuthHandler auth = new AuthHandler(username, password);
                    // TODO: for now we only return if user is authenticated
                    boolean isAuthenticated = auth.authenticate();

                    out.writeBoolean(isAuthenticated);
                    out.flush();
                    break;
                case GET_EXHIBITS:
                    ExhibitsHandler exhibitsHandler = new ExhibitsHandler();
                    List<Exhibit> exhibits = exhibitsHandler.getExhibits();
                    out.writeObject(exhibits);
                    out.flush();
                    break;
                case GET_EXHIBITIONS:
                    ExhibitionHandler exhibitionsHandler = new ExhibitionHandler();
                    String filter = (String)in.readObject();
                    List<Exhibition> exhibitions = exhibitionsHandler.getExhibitions(filter);
                    out.writeObject(exhibitions);
                    out.flush();
                    break;
                case INSERT_EXHIBITIONS:
                    exhibitionsHandler = new ExhibitionHandler();
                    Exhibition exhibition = (Exhibition)in.readObject();
                    Integer id = exhibitionsHandler.insertExhibition(exhibition);
                    out.writeInt(id);
                    out.flush();
                    break;



                default:
                    // TODO: make a res to this
                    System.out.println("Unexpected action");
                    break;
            }

            this.clientSocket.close();
        } catch (IOException | ClassNotFoundException  e) {
            System.err.println("Exception in client handler");
            e.printStackTrace();
        }
    }
}


