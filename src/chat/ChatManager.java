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
 * Class which partially contains logic of Chat application.
 *
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @created December 2019
 */
public class ChatManager {

    String mConnectedUsername;
    String mSelectedUsername;

    ArrayList<String> onlineUsersList = new ArrayList();
    ArrayList<String> chatRoomsList = new ArrayList();

    ArrayList<ChatMessage> messageStorage = new ArrayList();

    ArrayList<String> userReceivedMessage = new ArrayList();

    ChatUI mChatUI;

    /**
     * Creates instance of ChatUI.
     *
     * @param chatUI
     */
    public ChatManager(ChatUI chatUI) {
        mChatUI = chatUI;
    }

    /**
     * Method which returns online users ArrayList.
     *
     * @return
     */
    public ArrayList<String> getOnlineUsersList() {
        return onlineUsersList;
    }

    /**
     * Method which returns chat rooms ArrayList.
     *
     * @return
     */
    public ArrayList<String> getChatRoomsList() {
        return chatRoomsList;
    }

    /**
     * Method for adding received active users to online users ArrayList.
     *
     * @param data
     */
    public void addUserToList(String data) {
        onlineUsersList.add(data);
    }

    /**
     * Method for adding received chat room from server to chat room ArrayList.
     *
     * @param data
     */
    public void addChatroomToList(String data) {
        chatRoomsList.add(data);
    }

    /**
     * Method for clearing onlineUsers ArrayList.
     */
    public void clearUsers() {
        onlineUsersList.clear();
    }

    /**
     * Method for clearing ChatRooms ArrayList.
     */
    public void clearChatRooms() {
        chatRoomsList.clear();
    }

    /**
     * Method for clearing Users JList and ChatRooms JList.
     */
    public void clearLists() {
        DefaultListModel demoList = new DefaultListModel();
        mChatUI.clearLists(demoList);
    }

    /**
     * Method for cleaning MainChatArea.
     */
    public void clearMainChatArea() {
        mChatUI.clearMainChatArea();
    }

    /**
     * Method which returns mConnectedUsername string.
     *
     * @return
     */
    public String getUsername() {
        return mConnectedUsername;
    }

    /**
     * Method which sets user's username value to mConnectedUsername string.
     *
     * @param username
     */
    public void setUsername(String username) {
        mConnectedUsername = username;
    }

    /**
     * Method which returns mSelectedUsername string.
     *
     * @return
     */
    public String getSelectedUsername() {
        return mSelectedUsername;
    }

    /**
     * Method which sets a selected value from JLists to mSelectedUsername
     * string.
     *
     * @param username
     */
    public void setSelectedUsername(String username) {
        mSelectedUsername = username;
        displayMessages();
    }

    public ArrayList<String> getUserReceivedMessage() {
        return userReceivedMessage;
    }

    /**
     * Method for saving users in separate ArrayList via addUserToList method.
     * Invokes displayUsers method.
     *
     * @param data
     */
    public void setUsers(String[] data) {
        clearUsers();
        for (int i = 1; i <= data.length - 1; i++) {
            addUserToList(data[i]);
        }
        displayUsers();
    }

    /**
     * Method for saving chatRooms in separate ArrayList via addChatroomToList
     * method. Invokes displayChatRooms method.
     *
     * @param data
     */
    public void setChatRooms(String[] data) {
        clearChatRooms();
        for (int i = 1; i <= data.length - 1; i++) {
            addChatroomToList(data[i]);
        }
        displayChatRooms();
    }

    /**
     * Method that adds received online users to a temporary ArrayList, and
     * passes values to demoList. Invokes method from ChatUI class.
     */
    public void displayUsers() {
        String[] tempList = new String[(onlineUsersList.size())];
        onlineUsersList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();

        for (String token : tempList) {
            demoList.addElement(token);
        }

        mChatUI.displayUsers(demoList);
    }

    /**
     * Method that adds received chatRooms to a temporary ArrayList, and passes
     * values to demoList. Invokes method from ChatUI class.
     */
    public void displayChatRooms() {
        String[] tempList = new String[(chatRoomsList.size())];
        chatRoomsList.toArray(tempList);
        DefaultListModel demoList = new DefaultListModel();

        for (String token : tempList) {
            demoList.addElement(token);
        }
        mChatUI.displayChatRooms(demoList);
    }

    /**
     * Method for saving messages received from server.
     *
     * @param messageFromServer
     */
    public void saveReceivedMessage(String[] messageFromServer) {
        ChatMessage receivedChatMessage = new ChatMessage(messageFromServer[1], messageFromServer[3], messageFromServer[4]);
        messageStorage.add(receivedChatMessage);
        displayMessages();
    }

    /**
     * Method for saving messages sent by user.
     *
     * @param chatMessage
     */
    public void saveSentMessage(ChatMessage chatMessage) {
        messageStorage.add(chatMessage);
    }

    /**
     * Method for saving messages for a certain user in a separate ArrayList.
     */
    public void displayMessages() {
        ArrayList<ChatMessage> messagesForUser = new ArrayList();
        for (ChatMessage message : messageStorage) {
            if (message.getReceiver().equals(mSelectedUsername) || (message.getReceiver().equals(mConnectedUsername) && message.getSender().equals(mSelectedUsername))) {
                messagesForUser.add(message);
            }
        }
        mChatUI.displayMessages(messagesForUser);
    }

    /**
     * Method for saving messages only for a certain user or a group chat.
     *
     * @param data
     */
    public void processIncomingMessage(String[] data) {
        if (data[4].equals(mConnectedUsername) || data[4].equals("GroupChat")) {
            saveReceivedMessage(data);
        }

    }

    /**
     * Method which saves user who received a message to an array.
     *
     * @param data
     */
    public void saveUserReceivedMessage(String[] data) {
        if (data[4].equals("GroupChat")) {
            userReceivedMessage.add(data[4]);
        } else {
            userReceivedMessage.add(data[1]);
        }
    }
}
