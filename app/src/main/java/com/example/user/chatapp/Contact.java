package com.example.user.chatapp;


import java.util.ArrayList;

public class Contact {
    String username;
    ArrayList<String> contacts;

    public Contact(String username, ArrayList<String> contacts) {
        this.username = username;
        this.contacts=contacts;
    }

    public Contact(Contact value) {
        this.username=value.getUsername();
        this.contacts=value.getContacts();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public Contact() {
    }

}
