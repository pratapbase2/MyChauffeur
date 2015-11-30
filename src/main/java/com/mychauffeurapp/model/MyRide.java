package com.mychauffeurapp.model;


import android.media.Image;

public class MyRide {

    String source_address;
    String cost,date,uid,status;
    Image image;
    public MyRide(String uid,String cost,String date,String status){
        this.uid = uid;
        this.cost = cost;
        this.date = date;
        this.status = status;
    }

    public String getDate(){
        return date;
    }

    public String getCost() {return  cost; }

    public String getUniqueId() {return  uid; }

    public Image getImage() { return  image; }

    public String getStatus() {return  status; }
}
