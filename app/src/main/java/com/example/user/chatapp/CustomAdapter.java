package com.example.user.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Message> {
    public CustomAdapter(@NonNull Context context, ArrayList<Message> messages) {
        super(context, R.layout.custon_row,messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater messageInflater=LayoutInflater.from(getContext());
        View customView=messageInflater.inflate(R.layout.custon_row,parent,false);

        String message=this.getItem(position).getText();
        String sender=this.getItem(position).getSender();
        TextView messageView=(TextView) customView.findViewById(R.id.edit_text_custommessage);
        TextView senderView=(TextView) customView.findViewById(R.id.edit_text_customsender);

        messageView.setText(message);
        senderView.setText(sender);
        return customView;
    }
}
