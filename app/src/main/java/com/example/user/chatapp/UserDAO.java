package com.example.user.chatapp;

import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDAO {

    private DatabaseReference mDatabase;

    public  void InsertUser(User u){
        this.mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child(u.getUsername()).child("password").setValue(u.getPassword());

    }
    public boolean Validate(String username, String password){
        this.mDatabase=FirebaseDatabase.getInstance().getReference();
        //if(mDatabase.child(username).child("password"))
            return true;
       // return false;
    }
}
