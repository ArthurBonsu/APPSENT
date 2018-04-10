package com.gmail.peeman34.eglisaofficial;



/**
 * Created by pee on 9/21/2016.
 */

public class GeneralLister {



    private String title;
    private String image;

      public  GeneralLister(){}
    public  GeneralLister(String title, String image){
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
