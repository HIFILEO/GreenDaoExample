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

import greenDao.Message;
import greenDao.MessageDao;

/**
 * Adapter that takes a list of message objects and displays them to the listview.
 */
public class MessageAdapter extends ArrayAdapter<Message> implements View.OnClickListener {


    public MessageAdapter(Context context, int resource, List<Message> objects) {
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
        Message message = getItem(position);

        //Update Text View
        TextView textView = (TextView) convertView.findViewById(R.id.InfoTextView);
        textView.setText("Message ID: " + message.getIdentifier());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.DeleteImageView);
        imageView.setTag(position);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        //get conversation
        Message Message = getItem((Integer) view.getTag());

        //remove conversation from database
        MessageDao messageDao = GreenDaoExampleApplication.getDatabase().getMessageDao();
        try {
            messageDao.delete(Message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //remove from adapter
        remove(Message);
    }
}
