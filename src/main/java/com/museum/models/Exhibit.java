package com.museum.models;

import java.io.Serializable;
import java.sql.Date;

public class Exhibit implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public int exhibitID;
    public String name;
    public String status;
    public String description;
    public Date dateOfAcquisition;
    public int ageID;
    public double value;

    public Exhibit(int exhibitID, String name, String description, Date dateOfAcquisition, double value, int ageID) {
        this.exhibitID = exhibitID;
        this.name = name;
        this.description = description;
        this.dateOfAcquisition = dateOfAcquisition;
        this.value = value;
        this.ageID = ageID;

    }
}