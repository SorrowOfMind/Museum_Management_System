package com.museum.models;

import java.io.Serializable;

public class Worker_Basic implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public int workerID;
    public String forename ;
    public String surname;

    public Worker_Basic(int id, String forename, String surname){
        this.workerID = id;
        this.forename = forename;
        this.surname = surname;
    }

    public int getWorkerID(){
        return this.workerID;
    }

    @Override
    public String toString(){
        return this.surname.toUpperCase() + " " + this.forename;
    }
}
