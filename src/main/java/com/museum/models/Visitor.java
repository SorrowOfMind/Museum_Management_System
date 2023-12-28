package com.museum.models;

import java.io.Serializable;

public class Visitor implements Serializable {
    private static final long serialVersionUID = 6529685098267757680L;
    private String month;
    private int number;

    public Visitor(String month, int number) {
        this.month = month;
        this.number = number;
    }

    public String getMonth() {
        return month;
    }

    public int getNumber() {
        return number;
    }
}
