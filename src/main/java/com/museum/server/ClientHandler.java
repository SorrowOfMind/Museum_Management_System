package com.museum.server;

import com.museum.Actions;
import com.museum.models.Exhibit;
import com.museum.models.Exhibition;
import com.museum.models.Room;
import com.museum.models.Worker_Basic;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    // SOCKET
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    // HANDLERS
    private ExhibitsHandler exhibitsHandler;

    // DATA
    Exhibit exhibit;
    List<Exhibit> exhibits;
    byte[] imageData = null;

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
                    exhibitsHandler = new ExhibitsHandler();
                    exhibits = exhibitsHandler.getExhibits();
                    out.writeObject(exhibits);
                    out.flush();
                    break;
                case ADD_EXHIBIT:
                    exhibitsHandler = new ExhibitsHandler();
                    exhibit = (Exhibit) in.readObject();
                    Object receivedObject = in.readObject();
                    if (receivedObject instanceof byte[]) {
                        imageData = (byte[]) receivedObject;
                    }

                    exhibits = exhibitsHandler.addExhibit(exhibit, imageData);
                    out.writeObject(exhibits);
                    out.flush();
                    break;
                case UPDATE_EXHIBIT:
                    exhibitsHandler = new ExhibitsHandler();
                    exhibit = (Exhibit) in.readObject();
                    exhibits = exhibitsHandler.updateExhibit(exhibit);
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
                    Integer id = exhibitionsHandler.insertUpdateExhibition(exhibition, false);
                    out.writeInt(id);
                    out.flush();
                    break;
                case UPDATE_EXHIBITION:
                    exhibitionsHandler = new ExhibitionHandler();
                    Exhibition exhibitionToUpdate = (Exhibition)in.readObject();
                    exhibitionsHandler.insertUpdateExhibition(exhibitionToUpdate, true);
                    out.flush();
                    break;
                case GET_ROOMS:
                    exhibitionsHandler = new ExhibitionHandler();
                    List<Room> rooms = exhibitionsHandler.getRooms();
                    out.writeObject(rooms);
                    out.flush();
                    break;
                case GET_WORKERS_SIMPLIFIED:
                    exhibitionsHandler = new ExhibitionHandler();
                    List<Worker_Basic> workers = exhibitionsHandler.getWorkersForExhibition();
                    out.writeObject(workers);
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


