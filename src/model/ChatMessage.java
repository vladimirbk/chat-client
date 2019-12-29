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

    public ChatMessage(String username, String text, String recipient) {
        mSender = username;
        mReceiver = recipient;
        mText = text;
        mTimestamp = new Date().getTime();
    }

    public String getMessage() {
        return mText;
    }

    public String getTimestampString() {
        return dateFormat.format(new Date(mTimestamp));
    }

    public String getMessageForServer() {
        return "SENDMESSAGE" + Config.DELIMITER + mSender + Config.DELIMITER + mTimestamp + Config.DELIMITER + mText + Config.DELIMITER + mReceiver;
    }

    public String getMessageForChatArea(String username) {
        String message;
        if (getReceiver().equals(username)) {
            message = "[" + getTimestampString() + " from: " + mSender + "] " + mText + "\n";
        } else {
            message = "[" + getTimestampString() + " to: " + mReceiver + "] " + mText + "\n";
        }

        return message;
    }

    public String getSender() {
        return mSender;
    }

    public String getReceiver() {
        return mReceiver;
    }

    @Override
    public String toString() {
        return "Sender =" + mSender + " receiver=" + mReceiver + " text=" + mText + " ";
    }
}
