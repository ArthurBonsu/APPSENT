package com.gmail.peeman34.eglisaofficial;

/**
 * Created by pee on 4/9/2017.
 */

public class ChurchChosenLister {


    private String title;
    private String image;
    private  String location;

    public  ChurchChosenLister(){

    }

    public  ChurchChosenLister( String title, String image, String location){
        this.title = title;
        this.image = image;
        this.location = location;

    }

    public  String getTitle(){
        return  title;
    }

    public  String getImage(){
        return  image;

    }

 public  String getLocation(){return  location;}


    public  void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;

    }

 public  void setLocation(String location){
     this.location = location;
 }



}
