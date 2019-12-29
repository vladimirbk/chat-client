/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import utils.Config;

/**
 * Class which contains methods for message initialization, sending and display.
 * 
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @since December 2019
 */
public class ChatMessage {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String mText;
    private long mTimestamp;
    String mSender;
    String mReceiver;

    /**
     * Method for making a message an object.
     * @param username
     * @param text
     * @param recipient 
     */
    public ChatMessage(String username, String text, String recipient) {
        mSender = username;
        mReceiver = recipient;
        mText = text;
        mTimestamp = new Date().getTime();
    }

    /**
     * Method which returns text of a message.
     * @return 
     */
    public String getMessage() {
        return mText;
    }
    /**
     * Method which returns date of a message in a desired format.
     * @return 
     */
    public String getTimestampString() {
        return dateFormat.format(new Date(mTimestamp));
    }

    /**
     * Method which builds a message in a form of string which is going to be sent
     * to server.
     * @return 
     */
    public String getMessageForServer() {
        return "SENDMESSAGE" + Config.DELIMITER + mSender + Config.DELIMITER + mTimestamp + Config.DELIMITER + mText + Config.DELIMITER + mReceiver;
    }

    /**
     * Method which puts message in a right for for displaying to user.
     * @param username
     * @return 
     */
    public String getMessageForChatArea(String username) {
        String message;
        if (getReceiver().equals(username)) {
            message = "[" + getTimestampString() + " from: " + mSender + "] " + mText + "\n";
        } else if(getReceiver().equals("GroupChat") && getSender().equals(mSender)) {
            message = "[" + getTimestampString() + " " + mSender + " " + " in: " + mReceiver + "] " + mText + "\n";
        } else {   
            message = "[" + getTimestampString() + " to: " + mReceiver + "] " + mText + "\n";
        }

        return message;
    }

    /**
     * Method which returns sender of a message.
     * @return 
     */
    public String getSender() {
        return mSender;
    }

    /**
     * Method which returns receiver of a message.
     * @return 
     */
    public String getReceiver() {
        return mReceiver;
    }
}
