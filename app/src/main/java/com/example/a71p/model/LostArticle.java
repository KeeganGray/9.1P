package com.example.a71p.model;

public class LostArticle {

    public Integer ID;
    public String Condition;
    public String Name;
    public String Phone;
    public String Description;
    public String Date;
    public String Location;
    public LostArticle(Integer ID, String Condition, String Name, String Phone, String Description, String Date, String Location){
        this.ID=ID;
        this.Condition=Condition;
        this.Name=Name;
        this.Phone=Phone;
        this.Description=Description;
        this.Date=Date;
        this.Location=Location;
    }
    public LostArticle(){
    }


}
