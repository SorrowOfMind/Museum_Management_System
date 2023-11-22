package com.museum.models;

import javafx.collections.FXCollections;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Exhibition implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int exhibitionID;
    private String title ;
    private Date startDate ;
    private Date endDate;

    private ArrayList<Integer> exhibitIDs = new ArrayList<>();
    private ArrayList<Integer> roomIDs= new ArrayList<>();
    private ArrayList<Integer> workerIDs= new ArrayList<>();
    public Exhibition( String title, Date start, Date end, ArrayList<Integer> exhibitIDs, ArrayList<Integer> rooms, ArrayList<Integer> workers){
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.exhibitIDs = exhibitIDs;
        this.roomIDs = rooms;
        this.workerIDs = workers;

    }
    public Exhibition(int id, String title, Date start, Date end ){
        this.exhibitionID = id;
        this.title = title;
        this.startDate = start;
        this.endDate = end;

    }

    public Integer getExhibitionID() {
        return exhibitionID;
    }

    public String getTitle(){
        return this.title;
    }

    public Date getStartDate(){
        return this.startDate;
    }

    public Date getEndDate(){
        return this.endDate;
    }

    public ArrayList<Integer> getExhibitIds(){
        return exhibitIDs;
    }
    public ArrayList<Integer> getRoomIDs(){
        return roomIDs;
    }
    public ArrayList<Integer> getWorkerIDs(){
        return workerIDs;
    }

    public void appendIDs(Integer exhibit, Integer room, Integer worker){
        if(!this.exhibitIDs.contains(exhibit))
            this.exhibitIDs.add( exhibit);
        if(!this.roomIDs.contains(room))
            this.roomIDs.add( room);
        if(!this.workerIDs.contains(worker))
            this.workerIDs.add( worker);
    }
}
