package com.example.user.chatapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.chatapp.Message;
import com.example.user.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

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

        String currentUser=FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String message=this.getItem(position).getText();
        String sender=this.getItem(position).getSender();
        String date=this.getItem(position).getDate();

        TextView messageView=customView.findViewById(R.id.edit_text_custommessage);
        TextView senderView=customView.findViewById(R.id.edit_text_customsender);
        TextView dateView=customView.findViewById(R.id.textView_custom_date);
        messageView.setText(message);
        senderView.setText(sender);
        if(date.contains("p.m.")||date.contains("a.m."))
            date=date.substring(date.length()-12,date.length()-1)+"  "+date.substring(0,7);
        else
            date=date.substring(date.length()-8,date.length()-3)+"  "+date.substring(0,7);

        dateView.setText(date);

        if(sender.contains(" "))
            sender=sender.replace(" ","");
        if(!sender.equals(currentUser))
            customView.setBackgroundResource(R.drawable.rounded_corner);
        else
            customView.setBackgroundResource(R.drawable.rounded_corner_current);

        return customView;
    }
}
