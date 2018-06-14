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

import com.example.user.chatapp.ContactOrBlock;
import com.example.user.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomBlockAdapter extends ArrayAdapter<String> {

    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private String keyBlocks;
    private final ArrayList<String> BlockedUsers=new ArrayList<>();
    public CustomBlockAdapter(@NonNull Context context, @NonNull  ArrayList<String> blocked) {
        super(context, R.layout.custom_block_row,blocked);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final LayoutInflater messageInflater=LayoutInflater.from(getContext());
        String currentUser="";
        View customView = messageInflater.inflate(R.layout.custom_block_row,parent,false);
        if(mAuth.getCurrentUser().getEmail()!=null)
            currentUser=mAuth.getCurrentUser().getEmail();
        final String user=this.getItem(position);
        final TextView messageView= customView.findViewById(R.id.textView_custom_block);
        GetBlockedList(currentUser);
        messageView.setText(user);
        Button unblock=customView.findViewById(R.id.button_unblock);
        final String finalCurrentUser = currentUser;
        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unblock(finalCurrentUser,user);
            }
        });

        return customView;

    }

    private void Unblock(final String currentUser, String blockedUser) {

        GetBlockedList(currentUser);

        if(BlockedUsers.contains(blockedUser))
            BlockedUsers.remove(blockedUser);

        Toast.makeText(getContext(),BlockedUsers.toString(),Toast.LENGTH_SHORT).show();

        if(BlockedUsers!=null&&keyBlocks!=null)
            mDatabaseReference.child("Blocks").child(keyBlocks).setValue(new ContactOrBlock(currentUser,BlockedUsers));
    }
    private void GetBlockedList(final String currentUser){
        //BlockedUsers.clear();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot blockedUser:dataSnapshot.child("Blocks").getChildren()) {
                    ContactOrBlock aux = blockedUser.getValue(ContactOrBlock.class);
                    if (aux != null) {
                        if (aux.getUsername().equals(currentUser)) {
                            keyBlocks = blockedUser.getKey();
                            for (String e : aux.getUsersList())
                                if (!BlockedUsers.contains(e))
                                    BlockedUsers.add(e);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
