package com.example.android.diksha;

/**
 * Created by Lenovo on 18-02-2017.
 */

public class Words  {
    private  String event;
    private long date;
    private int alert;

    public Words(String event, long date, int alert){
        this.event = event;
        this.date = date;
        this.alert = alert;
    }

    private String getEvent(){
        return event;
    }

    private  long getDate(){
        return date;
    }

    private int getAlert(){
        return alert;
    }
}
