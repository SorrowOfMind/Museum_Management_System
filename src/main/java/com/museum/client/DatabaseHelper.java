package com.museum.client;

import com.museum.Actions;
import com.museum.DataModels.Age;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DatabaseHelper<T extends Age> {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    DatabaseHelper(Socket socket){
        this.socket = socket;
    }

    public T ExecuteDatabaseQuery(String query)  throws IOException, ClassNotFoundException {
        T object = null;
        try{
            out = new ObjectOutputStream(this.socket.getOutputStream());
            out.writeObject(Actions.RUNQUERY);
            out.writeObject(query);
            in = new ObjectInputStream(this.socket.getInputStream());
            object = (T) in.readObject();
            System.out.println(object);
        }catch(IOException | ClassNotFoundException e){
            System.out.println("Didnt work");
        }
    return object;

    }






}
