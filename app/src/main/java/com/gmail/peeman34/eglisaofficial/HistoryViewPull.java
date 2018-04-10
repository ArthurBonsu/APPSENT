package com.gmail.peeman34.eglisaofficial;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pee on 8/1/2016.
 */

class HistoryViewPull {

    private String title;
    private String desc;
    private  String image;


    public HistoryViewPull(){}

    public  HistoryViewPull(String title, String image, String desc ){
        this.title = title;
        this.desc = desc;
        this.image = image;
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


    public  void setTitle(String title){
        this.title = title;
    }
    public void setImage(String image){
        this.image = image;

    }
    public void setDesc(String desc){
        this.desc = desc;

    }





}
