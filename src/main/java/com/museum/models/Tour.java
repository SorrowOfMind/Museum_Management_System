package com.museum.models;


import java.sql.Date;

public class Tour {
    public Integer tourID;
    public String groupLeader;
    public Date tourDate;
    public Integer size;
    public String language;
    public Integer workerID;
    public String tourHour;

    private Integer standardTicketCount;
    private Integer discountTicketCount;

    // Constructor
    public Tour( String groupLeader, Date tourDate,String time, int size, String language, int workerID, Integer standardTicketCount, Integer discountTicketCount) {
        this.tourID = null;
        this.groupLeader = groupLeader;
        this.tourDate = tourDate;
        this.size = size;
        this.language = language;
        this.workerID = workerID;
        this.standardTicketCount = standardTicketCount;
        this.discountTicketCount = discountTicketCount;
        this.tourHour = time;
    }
    public Tour(int tourID, String groupLeader, Date tourDate, int size, String language, int workerID) {
        this.tourID = tourID;
        this.groupLeader = groupLeader;
        this.tourDate = tourDate;
        this.size = size;
        this.language = language;
        this.workerID = workerID;
    }

    // Getter methods
    public Integer getTourID() {
        return tourID;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public Date getTourDate() {
        return tourDate;
    }

    public int getSize() {
        return size;
    }

    public String getLanguage() {
        return language;
    }

    public int getWorkerID() {
        return workerID;
    }

}
