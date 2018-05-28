package com.example.user.chatapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.chatapp.CustomAdapter;
import com.example.user.chatapp.Encrypt;
import com.example.user.chatapp.Message;
import com.example.user.chatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private Button Send;
    ListView ListMessages;
    private DatabaseReference dref;
    //ArrayList<Message> messages=new ArrayList<>();
    ArrayList<Message> messages=new ArrayList<>();
    private EditText messageTex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        dref= FirebaseDatabase.getInstance().getReference();
        ListMessages=findViewById(R.id.list_view_allmessages);
        messageTex=(EditText)findViewById(R.id.edit_text_newtext);
        Send=(Button)findViewById(R.id.button_send);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean encryptedSuccessful;
                Message newMessage=new Message();
                newMessage.setSender(getIntent().getStringExtra("currentUser"));

                newMessage.setReceiver(getIntent().getStringExtra("clickedUser"));
                if(newMessage.getReceiver().compareTo(newMessage.getSender())<0)
                    newMessage.setPassword(newMessage.getSender()+newMessage.getReceiver());
                else
                    newMessage.setPassword(newMessage.getReceiver()+newMessage.getSender());
                try {
                    String encryptedMessage= Encrypt.encrypts(messageTex.getText().toString(),newMessage.getPassword());
                    newMessage.setText(encryptedMessage);
                    encryptedSuccessful=true;
                } catch (Exception e) {
                    e.printStackTrace();
                    encryptedSuccessful=false;
                }
                if(encryptedSuccessful)
                    dref.child("Messages").child(dref.push().getKey()).setValue(newMessage);
                else
                    Toast.makeText(getApplicationContext(),newMessage.getText()+encryptedSuccessful,Toast.LENGTH_SHORT).show();
                messageTex.setText("");


            }
        });
    }

    protected void onStart() {
        super.onStart();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean decryptedSuccsessful;
                messages.clear();
                String clickedUser=getIntent().getStringExtra("clickedUser");
                String currentUser=getIntent().getStringExtra("currentUser");
                for( DataSnapshot mSnapshot: dataSnapshot.child("Messages").getChildren()){
                    Message text=new Message(mSnapshot.getValue(Message.class));
                    if(text.getText()!=null&&text.getReceiver()!=null&&text.getSender()!=null)
                    if((text.getReceiver().equals(clickedUser)&&
                            text.getSender().equals(currentUser))||(
                        text.getSender().equals(clickedUser)&&
                        text.getReceiver().equals(currentUser)
                            )) {
                        try {
                            text.setText(Encrypt.decrypts(text.getText(),text.getPassword()));
                            decryptedSuccsessful=true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            decryptedSuccsessful=false;
                        }
                        if(decryptedSuccsessful)
                            messages.add(text);
                        else
                            Toast.makeText(getApplicationContext(),text.getPassword()+decryptedSuccsessful,Toast.LENGTH_SHORT).show();
                    }
                }
                final ArrayAdapter<Message> adapter2=new CustomAdapter(MessageActivity.this,messages);
                //final ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MessageActivity.this,android.R.layout.simple_dropdown_item_1line,messages);
                ListMessages.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    }
