/*
Property of Daniel Leonardis 2015.
Free to distribute, use, or modify under open source license
*/
package com.example.leonardis.greendaoexample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardis.greendaoexample.R;
import com.example.leonardis.greendaoexample.application.GreenDaoExampleApplication;

import java.util.List;

import greenDao.Conversation;
import greenDao.ConversationDao;
import greenDao.Message;
import greenDao.MessageDao;

/**
 * Adapter that takes a list of Conversation objects and displays them to the listview.
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> implements View.OnClickListener {

    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_cell, null);
            convertView.findViewById(R.id.DeleteImageView).setOnClickListener(this);
        }

        //Get conversation
        Conversation conversation = getItem(position);

        //Update Text View
        TextView textView = (TextView) convertView.findViewById(R.id.InfoTextView);
        textView.setText("Conversation ID: " + conversation.getIdentifier());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.DeleteImageView);
        imageView.setTag(position);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        //get conversation
        Conversation conversation = getItem((Integer) view.getTag());

        /*
        One of the major downfalls with greenDao is that it does not allow you the option of supporting delete cascades unless you write the sql code yourself.
        They did this on purpose and want you to delete from bottom up.
         */
        //Because delete cascade is not supported, we need the messages in database. However, this object does not have them. SO, we must go fetch them again by
        //calling reset. Otherwise they are cached.
        conversation.resetMessages();

        //Delete each message by hand
        MessageDao messageDao = GreenDaoExampleApplication.getDatabase().getMessageDao();
        for (Message message : conversation.getMessages()) {
            messageDao.delete(message);
        }

        //remove conversation from database
        ConversationDao conversationDao = GreenDaoExampleApplication.getDatabase().getConversationDao();
        try {
            conversationDao.delete(conversation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //remove from adapter
        remove(conversation);
    }
}
