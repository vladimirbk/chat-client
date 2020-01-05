/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import utils.Config;
import chat.ChatManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import model.ChatMessage;

/**
 * Class which contains UI and related methods and listeners.
 *
 * @author Vladimir Badashkhanov
 * @version 1.0
 * @created December 2019
 */
public class ChatWindow extends javax.swing.JFrame implements ChatUI {

    String username;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    Boolean isConnected = false;
    ChatManager chatManager;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Creates new form ChatWindow
     */
    public ChatWindow() {
        initComponents();
        initSettings();
        chatManager = new ChatManager(this);
    }

    /**
     * Method which display users in JList.
     *
     * @param listModel users list
     */
    @Override
    public void displayUsers(ListModel listModel) {
        userDisplayList.setModel(listModel);
    }

    /**
     * Method which display chatRooms in JList.
     *
     * @param listModel chat rooms list
     */
    @Override
    public void displayChatRooms(ListModel listModel) {
        chatRoomDisplayList.setModel(listModel);
    }

    /**
     * Method which clears JLists.
     *
     * @param listModel users and chat rooms list
     */
    @Override
    public void clearLists(ListModel listModel) {
        userDisplayList.setModel(listModel);
        chatRoomDisplayList.setModel(listModel);
    }

    /**
     * Method which clears main chat area.
     */
    @Override
    public void clearMainChatArea() {
        mainChatArea.selectAll();
        mainChatArea.replaceSelection("");
    }

    /**
     * Method which display messages sent to a user or a chat room.
     *
     * @param messages messages for certain user
     */
    @Override
    public void displayMessages(ArrayList<ChatMessage> messages) {
        mainChatArea.setText("");
        for (ChatMessage chatMessage : messages) {
            mainChatArea.append(chatMessage.getMessageForChatArea(chatManager.getUsername()));
        }
    }

    /**
     * Class for processing incoming requests from server.
     */
    public class IncomingReader implements Runnable {

        public void run() {
            String stream;
            String[] data;
            String info = "INFO", users = "USERS", chatRooms = "CHATROOMS", sendMessage = "SENDMESSAGE", error = "ERROR", logout = "LOGOUT";
            try {
                while ((stream = reader.readLine()) != null) {
                    data = stream.split(Config.DELIMITER);
                    if (data[0].equals(info)) {
                        //TODO
                    } else if (data[0].equals(users)) {
                        chatManager.setUsers(data);
                    } else if (data[0].equals(chatRooms)) {
                        chatManager.setChatRooms(data);
                    } else if (data[0].equals(sendMessage)) {
                        chatManager.processIncomingMessage(data);
                        chatManager.saveUserReceivedMessage(data);
                        newMessageNotification(userDisplayList);
                        newMessageNotification(chatRoomDisplayList);
                    } else if (data[0].equals(error)) {
                        mainChatArea.append(data[1] + "\n");
                    } else if (data[0].equals(logout)) {
                        mainChatArea.append(data[1] + "\n");
                    }
                }
            } catch (Exception e) {
                //mainChatArea.append("Connection with server was lost.\n");
            }
        }
    }

    /**
     * Method which invokes incoming reader via thread.
     */
    public void ListenThread() {
        Thread IncomingReader = new Thread(new IncomingReader());
        IncomingReader.start();
    }

    /**
     * Method which sends disconnect request to server.
     */
    public void sendDisconnect() {
        String userDisconnected = ("LOGOUT" + Config.DELIMITER + username);
        try {
            writer.println(userDisconnected);
            writer.flush();
        } catch (Exception e) {
            mainChatArea.append("Could not send disconnect request.\n");
        }
        isConnected = false;
        usernameArea.setEditable(true);
    }

    /**
     * Method which closes socket.
     */
    public void Disconnect() {
        try {
            socket.close();
        } catch (Exception e) {
            mainChatArea.append("Failed to disconnect.\n");
        }

        chatManager.clearLists();
        isConnected = false;
        usernameArea.setEditable(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        usernameLbl = new javax.swing.JLabel();
        usernameArea = new javax.swing.JTextField();
        connectBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainChatArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        msgInputArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        disconnectBtn = new javax.swing.JButton();
        chatNameLabel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        userDisplayList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        chatRoomDisplayList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChatWindow");

        usernameLbl.setText("Username:");

        connectBtn.setText("Connect");
        connectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectBtnActionPerformed(evt);
            }
        });

        userDisplayList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedUsername = chatManager.getOnlineUsersList().get(userDisplayList.getSelectedIndex());
                chatNameLabel.setText(selectedUsername);
                chatManager.setSelectedUsername(selectedUsername);
                chatManager.getUserReceivedMessage().remove(chatNameLabel.getText());
            }
        });

        chatRoomDisplayList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedChatRoom = chatManager.getChatRoomsList().get(chatRoomDisplayList.getSelectedIndex());
                chatNameLabel.setText(selectedChatRoom);
                chatManager.setSelectedUsername(selectedChatRoom);
                chatManager.getUserReceivedMessage().remove(chatNameLabel.getText());
            }
        });

        userDisplayList.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                userDisplayList.clearSelection();
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        chatRoomDisplayList.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                chatRoomDisplayList.clearSelection();
            }
        });

        mainChatArea.setEditable(false);
        mainChatArea.setColumns(20);
        mainChatArea.setLineWrap(true);
        mainChatArea.setRows(5);
        jScrollPane1.setViewportView(mainChatArea);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Online Users");

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        msgInputArea.setColumns(20);
        msgInputArea.setLineWrap(true);
        msgInputArea.setRows(5);
        jScrollPane4.setViewportView(msgInputArea);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Chat rooms");

        disconnectBtn.setText("Disconnect");
        disconnectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectBtnActionPerformed(evt);
            }
        });

        chatNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chatNameLabel.setText("Choose who to chat with");
        chatNameLabel.setToolTipText("");

        jScrollPane5.setViewportView(userDisplayList);

        jScrollPane6.setViewportView(chatRoomDisplayList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(usernameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(usernameArea, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(34, 34, 34)
                                                                .addComponent(connectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(disconnectBtn)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(6, 6, 6)
                                                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                                                .addContainerGap())
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(chatNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(29, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(usernameArea, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(connectBtn)
                                        .addComponent(disconnectBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(chatNameLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane4)
                                                        .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    /**
     * Method which sends LOGIN request to server and therefore connects to
     * server.
     *
     * @param evt click event
     */
    private void connectBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (isConnected == false) {
            username = usernameArea.getText();
            chatManager.setUsername(username);
            usernameArea.setEditable(false);
            clearMainChatArea();

            try {
                socket = new Socket("127.0.0.1", 5000);
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(socket.getOutputStream());
                writer.println("LOGIN" + Config.DELIMITER + chatManager.getUsername());
                writer.println("GETUSERS");
                writer.println("GETCHATROOMS");
                writer.flush();
                isConnected = true;
            } catch (Exception e) {
                mainChatArea.append("Connection failure. Please try again.\n");
                usernameArea.setEditable(true);
            }
            ListenThread();
        } else if (isConnected == true) {
            mainChatArea.append("You are already connected.\n");
        }
    }

    /**
     * Method which sends message written by user to a server.
     *
     * @param evt click event
     */
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String emptyField = "";
        String recipient = chatNameLabel.getText();
        ChatMessage chatMessage = new ChatMessage(chatManager.getUsername(), msgInputArea.getText(), recipient);

        if (msgInputArea.getText().equals(emptyField)) {
            msgInputArea.setText("");
            msgInputArea.requestFocus();
        } else if (recipient.equals("Choose who to chat with")) {
            mainChatArea.append("Choose who to chat with" + "\n");
        } else {
            try {
                writer.println(chatMessage.getMessageForServer());

                if (!recipient.equals("Choose who to chat with") && !recipient.equals("GroupChat") && !username.equals(recipient)) {
                    chatManager.saveSentMessage(chatMessage);
                }

                mainChatArea.append(chatMessage.getMessageForChatArea(chatManager.getUsername()));
                writer.flush();
            } catch (Exception e) {
                msgInputArea.append("Message was not sent.\n");
            }
            msgInputArea.setText("");
            msgInputArea.requestFocus();
        }
        msgInputArea.setText("");
        msgInputArea.requestFocus();
    }

    /**
     * Method that invokes methods related to disconnect functionality after
     * clicking on a disconnect button.
     *
     * @param evt click event
     */
    private void disconnectBtnActionPerformed(java.awt.event.ActionEvent evt) {
        clearMainChatArea();
        sendDisconnect();
        Disconnect();
    }

    /**
     * Implementation of a custom listener for frame's closing operation
     */
    WindowListener exitListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            sendDisconnect();
            System.exit(0);
        }
    };

    /**
     * Method which contains settings of a frame
     */
    private void initSettings() {
        this.addWindowListener(exitListener);
    }

    /**
     * Custom renderer for changing background color of certain JList items.
     *
     * @param list JList
     */
    private void newMessageNotification(JList list) {
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (chatManager.getUserReceivedMessage().contains(value.toString())) {
                    setBackground(Color.GREEN);
                }
                return c;
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel chatNameLabel;
    private javax.swing.JList<String> chatRoomDisplayList;
    private javax.swing.JButton connectBtn;
    private javax.swing.JButton disconnectBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea mainChatArea;
    private javax.swing.JTextArea msgInputArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JList<String> userDisplayList;
    private javax.swing.JTextField usernameArea;
    private javax.swing.JLabel usernameLbl;
    // End of variables declaration
}
