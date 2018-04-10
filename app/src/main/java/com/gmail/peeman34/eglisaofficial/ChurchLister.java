package com.gmail.peeman34.eglisaofficial;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pee on 1/5/2017.
 */

public class ChurchLister {


    private String title;
    private String desc;
    private  String image;
    private  String location;
    private  String date;

    private  String  username;

      public  ChurchLister(){

      }


    public  ChurchLister(String title, String desc, String image, String location, String date, String username){
        this.title =title;
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.username = username;
         this.location = location;


    }

    public  String getTitle(){
        return  title;
    }

     public String getLocation(){
         return location;
     }


    public  String getImage(){
        return  image;

    }


    public  String getDesc(){
        return desc;

    }
    public  String getDate(){
        return  date;

    }

    public  String getUsername(){
        return  username;

    }


    public  void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;

    }

    public void setDesc(String desc){
        this.desc = desc;

    }

    public void setDate(String date){
        this.date = date;

    }

    public  void setLocation(String location)

    {
     this.location = location;
    }

    public void setUsername(String username){
        this.username = username;

    }


//    public  void mainviewtitle(){

  //  }


}
