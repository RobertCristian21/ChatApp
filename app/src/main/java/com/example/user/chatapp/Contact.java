package com.example.user.chatapp;


import java.util.ArrayList;

public class Contact {
    String username;
    ArrayList<String> usersList=new ArrayList<>();

    public Contact(String username, ArrayList<String> usersList) {
        this.username = username;
        for( String e: usersList)
            this.AddValue(e);
    }
    public void AddValue(String val){
        usersList.add(val);
    }
    public Contact(Contact value) {
        this.username=value.getUsername();
        for (String e:value.getUsersList())
            this.AddValue(e);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }

    public void setContacts(ArrayList<String> usersList) {
        for (String e:usersList)
            this.AddValue(e);
    }

    public Contact() {
    }

}
