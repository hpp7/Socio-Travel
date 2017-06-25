package com.cockpunchers.commutator2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.string;
import android.app.Application;

import com.quickblox.module.chat.QBChatRoom;
import com.quickblox.module.users.model.QBUser;

public class App extends Application {

    private QBUser qbUser;
    private int currentPage = 1;
    private Map<String, QBUser> allQbUsers = new HashMap<String, QBUser>();
    private Map<String, List<ChatMessage>> allMessages = new HashMap<String, List<ChatMessage>>();
    private QBChatRoom currentRoom;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public QBUser getQbUser() {
        return qbUser;
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    public List<QBUser> getAllQbUsers() {
        List<QBUser> qbUsers = new ArrayList<QBUser>(allQbUsers.values());
        Collections.sort(qbUsers, new Comparator<QBUser>() {
            @Override
            public int compare(QBUser lhs, QBUser rhs) {
                return (int) Math.signum(lhs.getId() - rhs.getId());
            }
        });
        return qbUsers;
    }

    public void addQBUsers(QBUser... qbUsers) {
        for (QBUser qbUser : qbUsers) {
            allQbUsers.put(qbUser.getLogin(), qbUser);
        }
    }

    public List<ChatMessage> getMessages(string full_name) {
        return allMessages.get(full_name);
    }

    public void addMessage(String login, ChatMessage message) {
        List<ChatMessage> messages = allMessages.get(login);
        if (messages == null) {
            messages = new ArrayList<ChatMessage>();
            allMessages.put(login, messages);
        }
        messages.add(message);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public QBChatRoom getCurrentRoom() {
        
    	return currentRoom;
    }


    public void setCurrentRoom(QBChatRoom room) {
        this.currentRoom = room;
    }
}
