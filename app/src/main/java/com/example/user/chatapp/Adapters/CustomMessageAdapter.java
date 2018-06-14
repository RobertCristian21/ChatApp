package com.example.user.chatapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.chatapp.Message;
import com.example.user.chatapp.R;

import java.util.ArrayList;

public class CustomMessageAdapter extends ArrayAdapter<Message> {
    public CustomMessageAdapter(@NonNull Context context, ArrayList<Message> messages) {
        super(context, R.layout.custom_message_row,messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater messageInflater=LayoutInflater.from(getContext());
        View customView=messageInflater.inflate(R.layout.custom_message_row,parent,false);

        String message=this.getItem(position).getText();
        String sender=this.getItem(position).getSender();
        TextView messageView=customView.findViewById(R.id.edit_text_custommessage);
        TextView senderView=customView.findViewById(R.id.edit_text_customsender);

        messageView.setText(message);
        senderView.setText(sender);
        return customView;
    }
}
