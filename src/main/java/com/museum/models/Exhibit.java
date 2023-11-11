package com.museum.models;

import java.io.Serializable;

public class Exhibit implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public int id;
    public String name;
    public String status;

    public Exhibit(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
}