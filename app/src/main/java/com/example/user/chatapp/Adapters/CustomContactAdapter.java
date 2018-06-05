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
import com.example.user.chatapp.ContactOrBlock;
import com.example.user.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;


public class CustomContactAdapter extends ArrayAdapter<String> {
    String keyContacts;
    ArrayList<String> Users=new ArrayList<>();
    ArrayList<String> Blocks=new ArrayList<>();
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
     String keyBlocks;

    public CustomContactAdapter(@NonNull Context context, @NonNull ArrayList<String> contacts) {
        super(context, R.layout.custom_contact_row,contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final LayoutInflater messageInflater=LayoutInflater.from(getContext());
        View customView = messageInflater.inflate(R.layout.custom_contact_row,parent,false);

        final String user=this.getItem(position);
        final TextView messageView= customView.findViewById(R.id.textView_custom_contact);

        messageView.setText(user);
        Button delete=customView.findViewById(R.id.button_delete);

        Button block=customView.findViewById(R.id.button_block);
        getContactList(mAuth.getCurrentUser().getEmail());

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteContact(mAuth.getCurrentUser().getEmail(),user);
            }
        });


        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockUser(mAuth.getCurrentUser().getEmail(),user);
            }
        });
        return customView;
    }

    private void getContactList(final String email) {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.child("Contacts").getChildren()) {
                    ContactOrBlock aux = mDataSnapshot.getValue(ContactOrBlock.class);
                    if (aux.getUsername().equals(email)) {
                        keyContacts = mDataSnapshot.getKey();
                        for (String e : aux.getUsersList()) {
                            Users.add(e);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       // Toast.makeText(getContext(),key+Users.toString(),Toast.LENGTH_SHORT).show();
    }


    private void DeleteContact(final String email, String user) {
        getContactList(email);
        if(Users.contains(user))
            Users.remove(user);
        dref.child("Contacts").child(keyContacts).setValue(new ContactOrBlock(email,Users));
       // Toast.makeText(getContext(),user+ " Successfully removed from contacts ",Toast.LENGTH_SHORT).show();
    }

    private void getBlockList(final String email) {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Blocks.clear();
                for (DataSnapshot mDataSnapshot : dataSnapshot.child("Blocks").getChildren()) {
                    ContactOrBlock aux = mDataSnapshot.getValue(ContactOrBlock.class);
                    if (aux.getUsername().equals(email)) {
                        keyBlocks = mDataSnapshot.getKey();
                        for (String e : aux.getUsersList()) {
                            Blocks.add(e);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Toast.makeText(getContext(),key+Users.toString(),Toast.LENGTH_SHORT).show();
    }


    private void BlockUser(final String email, String user) {
        getBlockList(email);
        if(Blocks.contains(user))
            Blocks.remove(user);
        else
            Blocks.add(user);
        if(keyBlocks!=null&&Blocks!=null)
        dref.child("Blocks").child(keyBlocks).setValue(new ContactOrBlock(email,Blocks));
        // Toast.makeText(getContext(),user+ " Successfully removed from contacts ",Toast.LENGTH_SHORT).show();
    }
}
