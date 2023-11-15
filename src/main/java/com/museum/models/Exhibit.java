package com.museum.models;

import java.io.Serializable;
import java.sql.Date;

public class Exhibit implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int exhibitID;
    private String name;
    private String author;
    private Date creationDate;
    private String origins;
    private String description;
    private Date acquisitionDate;
    private int value;
    private int ageID;
    private Date lastConservation;
    private Date nextConservation;
    private String status;
    private String security;

    public Exhibit(
            int exhibitID,
            String name,
            String author,
            Date creationDate,
            String origins,
            String description,
            Date acquisitionDate,
            int value,
            int ageID,
            Date lastConservation,
            Date nextConservation,
            String status,
            String security
    ) {
        this.exhibitID = exhibitID;
        this.name = name;
        this.author = author;
        this.creationDate = creationDate;
        this.origins = origins;
        this.description = description;
        this.acquisitionDate = acquisitionDate;
        this.value = value;
        this.ageID = ageID;
        this.lastConservation = lastConservation;
        this.nextConservation = nextConservation;
        this.status = status;
        this.security = security;
    }

    public int getExhibitID() {
        return exhibitID;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSecurity() {
        return security;
    }
}