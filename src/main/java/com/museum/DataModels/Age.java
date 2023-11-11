package com.museum.DataModels;

public class Age {
    public Age(Integer ageID, String name, Integer  startYear, Integer endYear){
        this.ageID = ageID;
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
    };
    public Integer ageID;
    public String name;
    public int  startYear;
    public int endYear;
}
