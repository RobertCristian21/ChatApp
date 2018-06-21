package com.example.user.chatapp;


public class Message {
    private String sender,receiver,text,date,algorithm;

    public Message(String sender, String receiver, String text,String date,String algorithm) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date = date;
        this.algorithm=algorithm;
    }
    public Message(){
    }
    public Message(Message a){
        this.setText(a.getText());
        this.setReceiver(a.getReceiver());
        this.setSender(a.getSender());
        this.setDate(a.getDate());
        this.setAlgorithm(a.getAlgorithm());
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        if (!(!Objects.equals(sender, message.sender) || !Objects.equals(receiver, message.receiver) || !Objects.equals(text, message.text) || !Objects.equals(password, message.password) || !Objects.equals(algorithm, message.algorithm)))
            return true;
        else return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(sender, receiver, text,password,algorithm);
    }*/
}

