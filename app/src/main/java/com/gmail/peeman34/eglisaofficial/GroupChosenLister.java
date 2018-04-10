package com.gmail.peeman34.eglisaofficial;

/**
 * Created by pee on 4/9/2017.
 */

public class GroupChosenLister {


    private String title;
    private String image;


    public  GroupChosenLister(){

    }

    public  GroupChosenLister( String title, String image){
        this.title = title;
        this.image = image;


    }

    public  String getTitle(){
        return  title;
    }

    public  String getImage(){
        return  image;

    }




    public  void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;

    }





}
