package com.gmail.peeman34.eglisaofficial;

/**
 * Created by pee on 7/14/2017.
 */

public class MembersList {

    private String title;
    private String image;
    private String uid;

    public  MembersList(){}

    public  MembersList(String title, String image, String uid){
        this.title = title;
        this.image = image;
        this.uid = uid;

    }

    public  String getTitle(){
        return  title;
    }
 public  String getUid(){return  uid;}
    public  String getImage(){
        return  image;

    }

    public  void setTitle(String title){
        this.title = title;
    }
    public void setImage(String image){
        this.image = image;

    }


    public  void mainviewtitle(){

    }

}
