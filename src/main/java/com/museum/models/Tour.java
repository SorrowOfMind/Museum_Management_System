package com.museum.models;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDate;

public class Tour implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public Integer tourID;
    public String groupLeader;
    public LocalDate tourDate;
    public Integer size;
    public String language;
    public Integer workerID;
    public String tourHour;

    private Integer standardTicketCount;
    private Integer discountTicketCount;

    // Constructor
    public Tour(String groupLeader, LocalDate tourDate, String time, int size, String language, int workerID, Integer standardTicketCount, Integer discountTicketCount) {
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
    public Tour(int tourID, String groupLeader,  LocalDate tourDate, String time, int size, String language,int workerID, Integer standardTicketCount, Integer discountTicketCount) {
        this.tourID = tourID;
        this.groupLeader = groupLeader;
        this.tourDate = tourDate;
        this.size = size;
        this.language = language;
        this.workerID = workerID;
        this.standardTicketCount = standardTicketCount;
        this.discountTicketCount = discountTicketCount;
        this.tourHour = time;
    }
    public void setTourID(Integer id){
        this.tourID = id;
    }
    // Getter methods
    public Integer getTourID() {
        return tourID;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public LocalDate getTourDate() {
        return tourDate;
    }
    public String getTourHour(){
        return this.tourHour;
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

    public Integer getDiscountTicketCount() {
        return discountTicketCount;
    }
    public Integer getStandardTicketCount(){
        return standardTicketCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tour myObject = (Tour) o;

        return new EqualsBuilder()
                .append(tourID, myObject.tourID)
                .append(groupLeader, myObject.groupLeader)
                .append(tourDate, myObject.tourDate)
                .append(size, myObject.size)
                .append(language, myObject.language)
                .append(workerID, myObject.workerID)
                .append(tourHour, myObject.tourHour)
                .append(standardTicketCount, myObject.standardTicketCount)
                .append(discountTicketCount, myObject.discountTicketCount)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tourID)
                .append(groupLeader)
                .append(tourDate)
                .append(size)
                .append(language)
                .append(workerID)
                .append(tourHour)
                .append(standardTicketCount)
                .append(discountTicketCount)
                .toHashCode();
    }
}
