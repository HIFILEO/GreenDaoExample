/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.model.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import greenDao.ContactDao;
import greenDao.ConversationDao;
import greenDao.ConversationGroupDao;
import greenDao.DaoMaster;
import greenDao.DaoSession;
import greenDao.MessageDao;

/**
 * Creates the databases by hooking into GreenDao. Also holds convenience methods for accessing DAOs
 */
public class DatabaseHelper extends DaoMaster.OpenHelper {

    public static String TAG = DatabaseHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "GreenDaoExample.db";
    private static final int DATABASE_VERSION = 1;
    private static DaoSession daoSession;

    /**
     * Use this static method to create the database helper and open it for writing
     * @param context
     */
    public static DatabaseHelper createAndOpen(Context context) {

        DatabaseHelper databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        return databaseHelper;
    }

    /**
     * Constructor
     */
    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Note - in greenDao, the super creates all the tables for you. Here to show a full example of how to override.
        Log.d(TAG,"Create Database.");

        //For older devices
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            onConfigure(database);
        }

        super.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /*
            http://stackoverflow.com/questions/13373170/greendao-schema-update-and-data-migration
            https://web.archive.org/web/20131102132942/http://www.androidanalyse.com/greendao-schema-migration/
        */
        //Note - grabbed this from DaoMaster. Remove this and provide custom code.
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(sqLiteDatabase, true);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(android.database.sqlite.SQLiteDatabase db) {
        super.onOpen(db);
        Log.d(TAG, "onOpen");
    }

    @Override
    public void onConfigure(SQLiteDatabase database) {
        Log.d(TAG,"onConfigure");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            database.setForeignKeyConstraintsEnabled(true);
        } else {
            database.execSQL("PRAGMA foreign_keys = ON");
        }
    }

    /**
     * Get the DAO session
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * Convenience
     */
    public ContactDao getContactDao() {
        return daoSession.getContactDao();
    }

    /**
     * Convenience
     */
    public MessageDao getMessageDao() {
        return daoSession.getMessageDao();
    }

    /**
     * Convenience
     */
    public ConversationDao getConversationDao() {
        return daoSession.getConversationDao();
    }

    /**
     * Convenience
     */
    public ConversationGroupDao getConversationGroupDao() {
        return daoSession.getConversationGroupDao();
    }
}
