package com.gmail.peeman34.eglisaofficial;



/**
 * Created by pee on 9/21/2016.
 */

public class LatestMessageViewPull {

    private String title;
    private String desc;
    private  String image;
    private  String date;
    private  String  username;
    private  String preacher;
    private  String audio;

     public  LatestMessageViewPull(){}

    public  LatestMessageViewPull(String audio, String preacher,   String title, String image, String desc, String date, String username){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.username = username;
         this.audio = audio;
        this.preacher = preacher;

    }

     public  String getAudio(){return  audio;
     }

    public  String getTitle(){
        return  title;
    }

    public  String getImage(){
        return  image;

    }

    public  String  getPreacher(){
        return preacher;
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


    public void setUsername(String username){
        this.username = username;

    }

     public  void setPreacher(String preacher){
         this.preacher = preacher;

     }

      public  void setAudio(String audio){
          this.audio = audio;
      }


}
