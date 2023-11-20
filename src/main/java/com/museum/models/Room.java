package com.museum.models;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int roomID;
    private int roomNumber;
    private int floor;
    private int area;

    public Room(int id, int number, int floor, int area){
        this.roomID = id;
        this.roomNumber = number;
        this.floor = floor;
    }

    public int getID(){
        return this.roomID;
    }

    public int getRoomNumber(){
        return this.roomNumber;
    }

    public int getFloor(){
        return this.floor;
    }
    public int getArea(){
        return this.area;
    }

    @Override
    public String toString() {
        return String.format("Piętro: %s, Sala: %s, powierzchnia: %s", this.floor, this.roomNumber, this.area);
    }
}
