package com.gmail.peeman34.eglisaofficial;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pee on 8/1/2016.
 */

class AlbumPhotographyList {

    private String title;
    private String desc;
    private  String image;
  private  String username;

    public  AlbumPhotographyList(){}

    public   AlbumPhotographyList(String title, String image, String desc, String username ){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;
    }

    public  String getTitle(){
        return  title;
    }


    public  String getImage(){
        return  image;

    }


    public  String getUsername(){
        return  username;

    }

    public  String getDesc(){
        return desc;

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

    public void setUsername(String username){
        this.username = username;

    }




}
