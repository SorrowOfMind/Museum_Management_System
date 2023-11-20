package com.museum.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Exhibition implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int exhibitionID;
    private String title ;
    private Date startDate ;
    private Date endDate;

    private ArrayList<Integer> exhibitIDs;
    private ArrayList<Integer> roomIDs;
    private ArrayList<Integer> workerIDs;
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
}
