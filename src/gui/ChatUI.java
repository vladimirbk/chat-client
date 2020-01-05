/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import javax.swing.ListModel;
import model.ChatMessage;

/**
 * Class which groups related to UI methods.
 *
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @created December 2019
 */
public interface ChatUI {

    /**
     * Method which displays users in a JFrame.
     *
     * @param listModel users list
     */
    public void displayUsers(ListModel listModel);

    /**
     * Method which displays chat rooms in a JFrame.
     *
     * @param listModel chat rooms list
     */
    public void displayChatRooms(ListModel listModel);

    /**
     * Method which fills JList with empty values.
     *
     * @param listModel users and chat rooms lists
     */
    public void clearLists(ListModel listModel);

    /**
     * Method for clearing main chat area.
     */
    public void clearMainChatArea();

    /**
     * Method which displays messages in main chat area.
     *
     * @param messageStorage processed messages received from server
     */
    public void displayMessages(ArrayList<ChatMessage> messageStorage);
}
