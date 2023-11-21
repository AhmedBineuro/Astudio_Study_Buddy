package com.example.iat359_finalproject;

//Simple class to hold an event's information
public class Event {
    public String name,dateTime,location;
    public long db_ID;
    public Event(String name,String dateTime,String location){
        this.name=name;
        this.dateTime=dateTime;
        this.location=location;
    }
}
