/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import gui.ChatUI;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import model.ChatMessage;

/**
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @since December 2019
 */
public class ChatManager {

    String mConnectedUsername;
    String mSelectedUsername;

    ArrayList<String> onlineUsersList = new ArrayList();
    ArrayList<String> chatRoomsList = new ArrayList();

    ArrayList<ChatMessage> messageStorage = new ArrayList();

    ChatUI mChatUI;

    public ChatManager(ChatUI chatUI) {
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

    public void clearLists() {
        DefaultListModel demoList = new DefaultListModel();
        mChatUI.clearLists(demoList);
    }

    public void clearMainChatArea() {
        mChatUI.clearMainChatArea();
    }

    public String getUsername() {
        return mConnectedUsername;
    }

    public void setUsername(String username) {
        mConnectedUsername = username;
    }

    public String getSelectedUsername() {
        return mSelectedUsername;
    }

    public void setSelectedUsername(String username) {
        mSelectedUsername = username;
        displayMessages();
    }

    public void setUsers(String[] data) {
        clearUsers();
        for (int i = 1; i <= data.length - 1; i++) {
            addUserToList(data[i]);
        }
        displayUsers();
    }

    public void setChatRooms(String[] data) {
        clearChatRooms();
        for (int i = 1; i <= data.length - 1; i++) {
            addChatroomToList(data[i]);
        }
        displayChatRooms();
    }

    public void displayUsers() {
        String[] tempList = new String[(onlineUsersList.size())];
        onlineUsersList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();

        for (String token : tempList) {
            demoList.addElement(token);
        }

        mChatUI.displayUsers(demoList);
    }

    public void displayChatRooms() {
        String[] tempList = new String[(chatRoomsList.size())];
        chatRoomsList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();

        for (String token : tempList) {
            demoList.addElement(token);
        }
        mChatUI.displayChatRooms(demoList);
    }

    public void saveReceivedMessage(String[] messageFromServer) {
        ChatMessage receivedChatMessage = new ChatMessage(messageFromServer[1], messageFromServer[3], messageFromServer[4]);
        messageStorage.add(receivedChatMessage);
        displayMessages();
    }

    public void saveSentMessage(ChatMessage chatMessage) {
        messageStorage.add(chatMessage);
    }

    public void displayMessages() {
        ArrayList<ChatMessage> messagesForUser = new ArrayList();
        for (ChatMessage message : messageStorage) {
            if (message.getReceiver().equals(mSelectedUsername) || (message.getReceiver().equals(mConnectedUsername) && message.getSender().equals(mSelectedUsername))) {
                messagesForUser.add(message);
            }
        }
        mChatUI.displayMessages(messagesForUser);
    }

    public void processIncomingMessage(String[] data) {
        if (data[4].equals(mConnectedUsername) || data[4].equals("GroupChat")) {
            saveReceivedMessage(data);
        }

    }
}
