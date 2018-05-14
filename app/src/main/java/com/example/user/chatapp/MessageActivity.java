package com.example.user.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
        ListMessages=(ListView)findViewById(R.id.ListMessageAll);
        messageTex=(EditText)findViewById(R.id.editTextNewText);
        Send=(Button)findViewById(R.id.buttonSend);

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message newMessage=new Message();
                newMessage.setSender(getIntent().getStringExtra("b"));
                newMessage.setReceiver(getIntent().getStringExtra("a"));
                newMessage.setText(messageTex.getText().toString());
                String id=dref.push().getKey();
                dref.child("Messages").child(id).setValue(newMessage);
                messageTex.setText("");
               // if(adapter.getCount()>1)
                 //   ListMessages.smoothScrollToPosition(adapter.getCount()-1);

            }
        });
    }

    protected void onStart() {
        super.onStart();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();
                String a=getIntent().getStringExtra("a");
                String b=getIntent().getStringExtra("b");
                for( DataSnapshot mSnapshot: dataSnapshot.child("Messages").getChildren()){
                    Message text=new Message(mSnapshot.getValue(Message.class));
                    if(text.getText()!=null&&text.getReceiver()!=null&&text.getSender()!=null)
                    if((text.getReceiver().equals(a)&&
                            text.getSender().equals(b))||(
                        text.getSender().equals(a)&&
                        text.getReceiver().equals(b)
                            )) {
                        messages.add(text);

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
