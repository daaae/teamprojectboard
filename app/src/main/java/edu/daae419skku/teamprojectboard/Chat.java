package edu.daae419skku.teamprojectboard;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daae0 on 2017-06-22.
 */

public class Chat implements Serializable {

    private String messageUser;
    private String messageText;
    private String messageTime;

    public Chat(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.messageTime = dateFormat.format(date);
    }

    public Chat() {}

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
