package chat;

import gui.ChatUI;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import utils.Config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author badashkhanov
 */



public class ChatManager {
    
    ArrayList<String> onlineUsersList = new ArrayList();
    ArrayList<String> chatRoomsList = new ArrayList();
    ArrayList<String> messageStorage = new ArrayList();
    
    ChatUI mChatUI;
    
    public ChatManager(ChatUI chatUI){
        mChatUI = chatUI;
    }
    
    public ArrayList<String> getOnlineUsersList() {
        return onlineUsersList;
    }
    public ArrayList<String> getChatRoomsList() {
        return chatRoomsList;
    }
    
    public void addUserToList(String data) {
        onlineUsersList.add(data);
    }
    
    public void addChatroomToList(String data) {
        chatRoomsList.add(data);
    }

    public void clearUsers() {
        onlineUsersList.clear();
    }

    public void clearChatRooms() {
        chatRoomsList.clear();
    }
    
    public void clearLists () {
        DefaultListModel demoList = new DefaultListModel();
        mChatUI.clearLists(demoList);
    }
    
    public void clearMainChatArea () {
        mChatUI.clearMainChatArea();
    }

    public void setUsers(String[] data) {
        clearUsers();
        for(int i = 1; i <= data.length-1; i++){
            addUserToList(data[i]);
        }
        displayUsers();
    }
    
    public void setChatRooms(String[] data) {
        clearChatRooms();
        for(int i = 1; i <= data.length-1; i++){
            addChatroomToList(data[i]);
        }
        displayChatRooms();
    }
    
    public void displayUsers() {
        String[] tempList = new String[(onlineUsersList.size())];
        onlineUsersList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();
 
        for(String token: tempList){
            demoList.addElement(token);
        }
        
        mChatUI.displayUsers(demoList);
    }
        
    public void displayChatRooms() {
        String [] tempList = new String[(chatRoomsList.size())];
        chatRoomsList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();
        
        for (String token: tempList){
            demoList.addElement(token);
        }
        mChatUI.displayChatRooms(demoList);
    }
    
    public void saveReceivedMessage(String[] data){
        String receivedMessage =data[2] + Config.DELIMITER + data[3] + Config.DELIMITER + data[1];
        messageStorage.add(receivedMessage);
        
        System.out.println(messageStorage.get(0));
    }
    
    public void saveSentMessage(String message) {
        messageStorage.add(message);
        System.out.println(message + "SAVED MESSAGE");
    }
    
    public void displayMessages(){
        String message = messageStorage.get(0);
        String[] data = message.split(Config.DELIMITER);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        mChatUI.displayMessages();
    }
}
