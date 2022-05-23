package com.example.a71p.model;

import java.io.Serializable;

public class LostArticle implements Serializable {

    public Integer ID;
    public String Condition;
    public String Name;
    public String Phone;
    public String Description;
    public String Date;
    public String Location;
    public Double Latitude;
    public Double Longitude;

    public LostArticle(Integer ID, String Condition, String Name, String Phone, String Description, String Date, String Location, Double Latitude, Double Longitude){
        this.ID=ID;
        this.Condition=Condition;
        this.Name=Name;
        this.Phone=Phone;
        this.Description=Description;
        this.Date=Date;
        this.Location=Location;
        this.Latitude=Latitude;
        this.Longitude=Longitude;

    }
    public LostArticle(){
    }


}
