package com.shivam.taxiapp;

public class AddRequest {
   // private String requestID;
    private String source;
    private String destination;
    private String passengers;
    private String journeydate;
    public AddRequest(){

    }

    public AddRequest( String source, String destination,String passengers,String journeydate) {
        //this.requestID= requestID;
        this.source=source;
        this.destination=destination;
        this.journeydate=journeydate;
        this.passengers=passengers;
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
}
