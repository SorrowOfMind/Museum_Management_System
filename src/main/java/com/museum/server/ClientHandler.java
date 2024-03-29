package com.museum.server;

import com.museum.Actions;
import com.museum.models.*;
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
    private WorkersHandler workersHandler;
    private DataHandler visitorsHandler;

    // DATA
    Exhibit exhibit;
    List<Exhibit> exhibits;
    List <Worker> workers;
    List<Visitor> visitorData;
    List<Exhibition> exhibitions;
    List<Tour> tours;

    AuthHandler auth;
    byte[] imageData = null;
    private Object receivedObject;
    Worker worker;

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

                    auth = new AuthHandler();
                    boolean isAuthenticated = auth.authenticate(username, password);
                    out.writeBoolean(isAuthenticated);

                    if (isAuthenticated) {
                        out.writeInt(auth.getRole());
                        out.writeObject(auth.getUser());
                    }

                    out.flush();
                    break;
                case CHANGE_PSWD:
                    int userID = (int) in.readObject();
                    String oldPassword = (String) in.readObject();
                    String newPassword = (String) in.readObject();

                    auth = new AuthHandler();
                    boolean isChanged = auth.changePassword(userID, oldPassword, newPassword);
                    out.writeBoolean(isChanged);
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
                    receivedObject = in.readObject();
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
                    receivedObject = in.readObject();
                    if (receivedObject instanceof byte[]) {
                        imageData = (byte[]) receivedObject;
                    }
                    exhibits = exhibitsHandler.updateExhibit(exhibit, imageData);
                    out.writeObject(exhibits);
                    out.flush();
                    break;
                case GET_EXHIBITIONS:
                    ExhibitionHandler exhibitionsHandler = new ExhibitionHandler();
                    List<Exhibition> exhibitions = exhibitionsHandler.getExhibitions();
                    out.writeObject(exhibitions);
                    out.flush();
                    break;
                case INSERT_EXHIBITIONS:
                    exhibitionsHandler = new ExhibitionHandler();
                    Exhibition exhibition = (Exhibition)in.readObject();
                    exhibitions = exhibitionsHandler.insertUpdateExhibition(exhibition, false);
                    out.writeObject(exhibitions);
                    out.flush();
                    break;
                case UPDATE_EXHIBITION:
                    exhibitionsHandler = new ExhibitionHandler();
                    Exhibition exhibitionToUpdate = (Exhibition)in.readObject();
                    exhibitions = exhibitionsHandler.insertUpdateExhibition(exhibitionToUpdate, true);
                    out.writeObject(exhibitions);
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
                    List<Worker_Basic> workersBasic = exhibitionsHandler.getWorkersForExhibition();
                    out.writeObject(workersBasic);
                    out.flush();
                    break;
                case GET_TOURS:
                    TourHandler tourHandler = new TourHandler();
                    tours = tourHandler.getTours();
                    out.writeObject(tours);
                    out.flush();
                    break;
                case INSERT_TOUR:
                    tourHandler = new TourHandler();
                    Tour tour = (Tour) in.readObject();
                    tours = tourHandler.insertUpdateTour(tour, false);
                    out.writeObject(tours);
                    out.flush();
                    break;
                case UPDATE_TOUR:
                    tourHandler = new TourHandler();
                    Tour tourToUpdate = (Tour) in.readObject();
                    tours = tourHandler.insertUpdateTour(tourToUpdate, true);
                    out.writeObject(tours);
                    out.flush();
                    break;
                case GET_WORKERS:
                    workersHandler = new WorkersHandler();
                    workers = workersHandler.getWorkers();
                    out.writeObject(workers);
                    out.flush();
                    break;
                case ADD_WORKER:_WORKERS:
                    workersHandler = new WorkersHandler();
                    worker = (Worker) in.readObject();
                    workers = workersHandler.addWorker(worker);
                    out.writeObject(workers);
                    out.flush();
                    break;
                case UPDATE_WORKER:
                    workersHandler = new WorkersHandler();
                    worker = (Worker) in.readObject();
                    workers = workersHandler.updateWorker(worker);
                    out.writeObject(workers);
                    out.flush();
                    break;
                case GET_VISITORS:
                    visitorsHandler = new DataHandler();
                    visitorData = visitorsHandler.getVisitorsData();
                    out.writeObject(visitorData);
                    out.flush();
                    break;
                default:
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


