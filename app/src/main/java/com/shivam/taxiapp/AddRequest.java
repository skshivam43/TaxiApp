package com.shivam.taxiapp;

public class AddRequest {
   // private String requestID;
    private String source;
    private String destination;
    private String passengers;
    private String journeydate;
    private String journeytime;


    public AddRequest( String source, String destination,String passengers,String journeydate, String journeytime) {
        //this.requestID= requestID;
        this.source=source;
        this.destination=destination;
        this.journeydate=journeydate;
        this.passengers=passengers;
        this.journeytime=journeytime;
    }

    public AddRequest()
    {

    }

    /* public String getRequestID() {
         return requestID;
     }
     public void setRequestID(String requestID)
     {
         this.requestID = requestID;
     }*/
    public String getSource() {
        return source;
    }
    public void setSource(String source)
    {
        this.source=source;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination)
    {
        this.destination=destination;
    }

    public String getJourneydate() {
        return journeydate;
    }
    public void setJourneydate(String journeydate)
    {
        this.journeydate=journeydate;
    }

    public String getPassengers() {
        return passengers;
    }
    public void setPassengers(String passengers)
    {
        this.passengers=passengers;
    }

    public String getJourneytime(){return journeytime;}

    public void setJourneytime(String journeytime) {
        this.journeytime = journeytime;
    }
}
