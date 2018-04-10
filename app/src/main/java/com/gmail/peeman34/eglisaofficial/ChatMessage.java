package com.gmail.peeman34.eglisaofficial;

import java.util.Date;

/**
 * Created by pee on 4/24/2017.
 */

public class ChatMessage {

    private String messageText;
    private String username;
    private long messageTime;
    private  String churchkey;
    private  String groupkey;

     public  ChatMessage(){}

    public ChatMessage(String messageText,  String username, String churchkey, String groupkey) {
        this.messageText = messageText;
        this.username = username;
         this.churchkey = churchkey;
        this.groupkey  = groupkey;

        // Initialize to current time
        messageTime = new Date().getTime();
    }


    public String getMessageText() {
        return messageText;
    }

    public  String getChurchkey(){
         return  churchkey;
    }

    public  String getGroupkey(){
        return  groupkey;
    }

    public long getMessageTime() {
        return messageTime;
    }


    public String getMessageUser() {
        return username;
    }




    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public void setMessageUser(String username) {
        this.username = username;
    }
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
    public  void setGroupkey(String groupkey){
        this.groupkey = groupkey;
    }

     public  void setChurchkey(String churchkey){
         this.churchkey = churchkey;
     }


}
