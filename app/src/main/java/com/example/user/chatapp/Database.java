package com.example.user.chatapp;

import android.support.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class Database {
    private DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private static FirebaseAuth mAuth;
    public ArrayList<String> GetUsers(String username){
        ArrayList<String> allUsers=new ArrayList<>();
        return allUsers;
    }
    public ArrayList<String> GetContacts(String username){
        ArrayList<String> allContacts=new ArrayList<>();
        return allContacts;
    }
    public boolean SendMessage(String currentUser,String clickedUser){
        boolean sent=true;
        return sent;
    }
    public boolean AddContact(String currentUser,String clickedUser){
        boolean added=true;
        return added;
    }
    public boolean Register(String username){
        boolean success=true;
        return success;
    }
    public boolean RemoveContact(String currentUser,String clickedUser){
        boolean success=true;
        return success;
    }
    public static boolean validate(String username, String password){
        final boolean[] ok = {false};
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ok[0] =true;
                }else{
                    ok[0] =false;
                }
            }
        });
        return ok[0];
    }
}
