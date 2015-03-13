/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.viewcontrollers.activites;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.leonardis.greendaoexample.R;
import com.example.leonardis.greendaoexample.adapters.MessageAdapter;
import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import greenDao.Conversation;
import greenDao.ConversationDao;
import greenDao.Message;
import greenDao.MessageDao;

/**
 * Shows a list of messages given the conversation ID sent in.
 */
public class MessageActivity extends ListActivity {
    public static final String TAG = MessageActivity.class.getSimpleName();
    private static final String CONVERSATION_ID = "Conversation_ID";
    private long conversationId;
    private Conversation conversation;

    /**
     * Start Activity
     * @param context
     * @param conversationId
     */
    public static void startActivity(Context context, long conversationId) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(CONVERSATION_ID, conversationId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        //Load conversation
        conversationId = getIntent().getExtras().getLong(MessageActivity.CONVERSATION_ID);
        ConversationDao conversationDao = GreenDaoExampleApplication.getDatabase().getConversationDao();

        try {
            conversation = conversationDao.load(conversationId);
        } catch (Exception e) {
            Log.e(TAG, " Issue loading conversation.", e);
            finish();
        }

        //
        //Listener
        //
        Button addButton = (Button) findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageDao messageDao = GreenDaoExampleApplication.getDatabase().getMessageDao();

                try {
                    long lastId = messageDao.count();
                    String messageId = "ABC" + (lastId + 1);

                    //create message and save to database
                    Message message = new Message();//Sender is the 1st entry in the list
                    message.setIdentifier(messageId);
                    message.setConversationId(conversation.getId());
                    message.setSenderId(conversation.getGroupMembers().get(0).getId());
                    messageDao.insert(message);

                    //add message to list
                    ((MessageAdapter)getListAdapter()).add(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //
        //Create adapter
        //
        /*
        Note - example of how the ONE to MANY is accessed
         */
        List<Message> messages = conversation.getMessages();
        MessageAdapter messageAdapter = new MessageAdapter(this, 0, new ArrayList<>(messages));
        setListAdapter(messageAdapter);
    }
}
