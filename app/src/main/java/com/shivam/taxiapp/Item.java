package com.shivam.taxiapp;

public class Item {

    String source;
    String destination;

    public Item(String source,String destination)
    {
        this.source=source;
        this.destination=destination;
    }
    public String getSource()
    {
        return source;
    }
    public String getDestination()
    {
        return destination;
    }
}