package com.gmail.peeman34.eglisaofficial;



/**
 * Created by pee on 9/21/2016.
 */

public class Announcement11 {

    private String title;
    private String desc;
    private  String image;
    private  String date;


    public  Announcement11(){}


    public  Announcement11(String title, String image, String desc, String date, String username){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;

    }

    public  String getTitle(){
        return  title;
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




}
