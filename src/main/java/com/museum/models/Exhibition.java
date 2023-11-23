package com.museum.models;

import javafx.collections.FXCollections;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    public void setExhibitionID(Integer id){
        this.exhibitionID = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exhibition myObject = (Exhibition) o;

        return new EqualsBuilder()
                .append(exhibitionID, myObject.exhibitionID)
                .append(title, myObject.title)
                .append(startDate, myObject.startDate)
                .append(endDate, myObject.endDate)
                .append(exhibitIDs, myObject.exhibitIDs)
                .append(roomIDs, myObject.roomIDs)
                .append(workerIDs, myObject.workerIDs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(exhibitionID)
                .append(title)
                .append(startDate)
                .append(endDate)
                .append(exhibitIDs)
                .append(roomIDs)
                .append(workerIDs)
                .toHashCode();
    }
}
