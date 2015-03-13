/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.viewcontrollers.activites;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.leonardis.greendaoexample.R;
import com.example.leonardis.greendaoexample.adapters.ConversationAdapter;
import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;
import com.example.leonardis.greendaoexample.services.CopyDatabaseService;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import greenDao.Contact;
import greenDao.ContactDao;
import greenDao.Conversation;
import greenDao.ConversationDao;

/**
 * Shows a list of conversations that are available
 */
public class ConversationActivity extends ListActivity {
    public static final String TAG = ConversationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);

        //
        //Listeners
        //
        Button addButton = (Button) findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationDao conversationDao = GreenDaoExampleApplication.getDatabase().getConversationDao();
                ContactDao contactDao = GreenDaoExampleApplication.getDatabase().getContactDao();
                try {
                    long lastId = conversationDao.count();

                    List<Contact> contactList = contactDao.loadAll();

                    //create group
                    List<Contact> groupMembers = new ArrayList<>();
                    groupMembers.add(contactList.get(0));
                    groupMembers.add(contactList.get(1));

                    //create conversation and save to database
                    String conversationId = "ABC123C" + (lastId + 1);
                    Conversation conversation = new Conversation();
                    conversation.setIdentifier(conversationId);
                    conversation.setGroupMembers(groupMembers);
                    conversationDao.insert(conversation);

                    //add conversation to list
                    ((ConversationAdapter)getListAdapter()).add(conversation);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //
        //Create Adapter
        //
        ConversationDao conversationDao = GreenDaoExampleApplication.getDatabase().getConversationDao();
        try {
            ConversationAdapter conversationAdapter = new ConversationAdapter(this, 0, conversationDao.loadAll() );
            setListAdapter(conversationAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(android.widget.ListView listView, View view, int position, long id) {
        Conversation conversation = (Conversation) getListAdapter().getItem(position);
        MessageActivity.startActivity(this, conversation.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.action_copy_database:
                Intent intent= new Intent(ConversationActivity.this, CopyDatabaseService.class);
                startService(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
