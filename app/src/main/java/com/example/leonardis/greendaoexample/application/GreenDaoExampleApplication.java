/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.application;

import android.app.Application;
import android.util.Log;

import com.example.leonardis.greendaoexample.model.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import greenDao.Contact;
import greenDao.ContactDao;
import greenDao.Conversation;
import greenDao.ConversationDao;
import greenDao.Message;
import greenDao.MessageDao;

/**
 * Represents the single application class
 */
public class GreenDaoExampleApplication extends Application {

    public static final String TAG = GreenDaoExampleApplication.class.getSimpleName();
    private static GreenDaoExampleApplication application;
    private static DatabaseHelper databaseHelper;

    /**
     * Setup logging information and hockey app crash manager.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //
        //Create Database
        //
        databaseHelper = DatabaseHelper.createAndOpen(this);

        //
        //Clear Tables on every start
        //
        try {
            databaseHelper.getMessageDao().deleteAll();
            databaseHelper.getConversationGroupDao().deleteAll();
            databaseHelper.getConversationDao().deleteAll();
            databaseHelper.getContactDao().deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        //Create default data
        //

        //Create 2 contacts and insert them into database

        Contact dan = new Contact();
        dan.setGUID("A1981");
        dan.setFirstName("Dan");
        dan.setLastName("LEO");

        Contact doug = new Contact();
        doug.setGUID("A1982");
        doug.setFirstName("Doug");
        doug.setLastName("Funny");

        ContactDao contactDao = databaseHelper.getContactDao();
        contactDao.insert(dan);
        contactDao.insert(doug);

        //Create 1 conversation between the two contacts and insert it into database.
        List<Contact> groupMembers = new ArrayList<>();
        groupMembers.add(dan);
        groupMembers.add(doug);

        Conversation conversation = new Conversation();
        conversation.setIdentifier("ABC123C1");
        conversation.setGroupMembers(groupMembers);
        ConversationDao conversationDao = GreenDaoExampleApplication.getDatabase().getConversationDao();

        try {
            conversationDao.insert(conversation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create 2 messages for the above conversation
        Message message1 = new Message();
        message1.setContact(dan);
        message1.setIdentifier("ABC1");
        Message message2 = new Message();
        message2.setContact(doug);
        message2.setIdentifier("ABC2");

        MessageDao messageDao = GreenDaoExampleApplication.getDatabase().getMessageDao();
        /*
        Note - the try catch has to be Exception and not SQLException. You don't need to put try catch but if you don't SQL inserts on duplicates will crash the system!
         */
        try {
            messageDao.insert(message1);
            messageDao.insert(message2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ease of access method to return the application class
     * @return
     */
    public static GreenDaoExampleApplication getInstance() {
        return application;
    }

    /***
     * Get database helper for application. If null, we attempt to open.
     * @return db - database helper or null
     */
    public static DatabaseHelper getDatabase() {
        if (databaseHelper == null) {
            Log.w(TAG, "Database was null, create failed!");
            databaseHelper = DatabaseHelper.createAndOpen(application);
        }

        return databaseHelper;
    }

    /***
     * Close Database and null out the handle.
     */
    public static void closeDatabase() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        databaseHelper = null;
    }
}
