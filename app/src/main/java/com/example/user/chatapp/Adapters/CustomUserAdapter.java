package com.example.user.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.chatapp.Activities.MessageActivity;
import com.example.user.chatapp.R;
import com.example.user.chatapp.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class CustomUserAdapter extends ArrayAdapter<String> {
    public CustomUserAdapter(@NonNull Context context, ArrayList<String> user) {
        super(context, R.layout.custom_users_row,user);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        LayoutInflater messageInflater=LayoutInflater.from(getContext());
        View customView=messageInflater.inflate(R.layout.custom_users_row,parent,false);

        final String user=this.getItem(position);
        final TextView messageView= customView.findViewById(R.id.textView_custom_user);

        final Button add_delete=customView.findViewById(R.id.button_add_delete);
        add_delete.setText("Add");
        messageView.setText(user);
        add_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MessageActivity.class);

                intent.putExtra("clickedUser", messageView.getText().toString());
                intent.putExtra("currentUser", mAuth.getCurrentUser().getEmail());
                Toast.makeText(getContext(), messageView.getText().toString(), Toast.LENGTH_SHORT).show();
                startActivity(getContext(),intent,null);
            }
        });

        return customView;
    }


}
