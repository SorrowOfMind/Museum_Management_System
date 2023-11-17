package com.museum.models;

public class Room {
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
        return floor + "\t\t" + roomNumber + "\t\t" + area;
    }
}
