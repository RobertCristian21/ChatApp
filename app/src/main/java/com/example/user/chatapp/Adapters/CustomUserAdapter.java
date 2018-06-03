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
import com.example.user.chatapp.Contact;
import com.example.user.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class CustomUserAdapter extends ArrayAdapter<String> {
    ArrayList<String> databaseContacts=new ArrayList<>();
    String key;
    public CustomUserAdapter(@NonNull Context context, ArrayList<String> user) {
        super(context, R.layout.custom_users_row,user);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final LayoutInflater messageInflater=LayoutInflater.from(getContext());
        View customView = messageInflater.inflate(R.layout.custom_users_row,parent,false);

        final String user=this.getItem(position);
        final TextView messageView= customView.findViewById(R.id.textView_custom_user);

        messageView.setText(user);
        databaseContacts=GetDatabaseContacts(mAuth.getCurrentUser().getEmail());


        final Button add_delete=customView.findViewById(R.id.button_add_delete);


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
        add_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToContacts(mAuth.getCurrentUser().getEmail(),user);
            }
        });
        return customView;
    }

    private void addToContacts(String email, String user) {
        Contact newContact=new Contact();
        newContact.setUsername(email);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
        ArrayList<String>previousContacts=new ArrayList<>();
        if(databaseContacts!=null&&key!=null){
            for (String e : databaseContacts)
                if (!previousContacts.contains(e))
                    previousContacts.add(e);
            if (!previousContacts.contains(user))
                previousContacts.add(user);
            newContact.setContacts(previousContacts);

            dref.child("Contacts").child(key).setValue(newContact);
        }
        else
        {
            previousContacts.add(user);
            newContact.setContacts(previousContacts);
            dref.child("Contacts").child(dref.push().getKey()).setValue(newContact);
        }

    }

    private ArrayList<String> GetDatabaseContacts(final String email) {


        DatabaseReference dref=FirebaseDatabase.getInstance().getReference();

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot mDataSnapshot:dataSnapshot.child("Contacts").getChildren()) {
                    Contact aux = mDataSnapshot.getValue(Contact.class);
                    if(aux.getUsername().equals(email)){
                        key=mDataSnapshot.getKey();
                        for( String e:aux.getUsersList()) {
                            if(!databaseContacts.contains(e))
                            databaseContacts.add(e);


                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return databaseContacts;

    }


}
