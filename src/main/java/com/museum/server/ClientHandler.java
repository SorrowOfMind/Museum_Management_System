package com.museum.server;

import com.museum.Actions;
import java.io.*;
import java.net.Socket;

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
                case RUNQUERY:
                    QueryHandler queryHandler = new QueryHandler();
                    String query = (String) in.readObject();
                    out.writeObject(queryHandler.ExecuteDatabaseQuery(query));

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
