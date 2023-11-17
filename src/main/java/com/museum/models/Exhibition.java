package com.museum.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public class Exhibition implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int exhibitionID;
    private String title ;
    private Date startDate ;
    private Date endDate;
    private int room;
    private ArrayList<Integer> exhibitIDs;
    public Exhibition( String title, Date start, Date end, ArrayList<Integer> exhibitIDs, int room){
        this.title = title;
        this.startDate = start;
        this.endDate = end;
        this.exhibitIDs = exhibitIDs;
        this.room = room;

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
}
