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
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @since December 2019
 */
public interface ChatUI {

    public void displayUsers(ListModel listModel);

    public void displayChatRooms(ListModel listModel);

    public void clearLists(ListModel listModel);

    public void clearMainChatArea();

    public void displayMessages(ArrayList<ChatMessage> messageStorage);
}
