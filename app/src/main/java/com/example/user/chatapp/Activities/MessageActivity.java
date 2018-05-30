package com.example.user.chatapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.text.BreakIterator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageActivity extends AppCompatActivity {
    private Button Send;
    ListView ListMessages;
    private DatabaseReference dref;
    ArrayList<Message> messages=new ArrayList<>();
    private EditText messageTex;
    String algorithm="AES";//used in encrypting the messages when sent
    String alg;//used in decrypting messages when reading them

    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        dref= FirebaseDatabase.getInstance().getReference();
        ListMessages=findViewById(R.id.list_view_allmessages);
        messageTex=findViewById(R.id.edit_text_newtext);
        Send=findViewById(R.id.button_send);

        Send.setOnClickListener(new View.OnClickListener() {
            String passAux;
            @Override
            public void onClick(View v) {
                boolean encryptedSuccessful;
                Message newMessage=new Message();
                newMessage.setSender(getIntent().getStringExtra("currentUser"));

                newMessage.setReceiver(getIntent().getStringExtra("clickedUser"));
                if(newMessage.getReceiver().compareTo(newMessage.getSender())>0)
                    passAux+=newMessage.getSender()+newMessage.getReceiver();
                else
                    passAux+=newMessage.getReceiver()+newMessage.getSender();
                try {
                    String encryptedMessage= Encrypt.encrypts(messageTex.getText().toString(),currentDateTimeString,algorithm);
                    newMessage.setText(encryptedMessage);
                    newMessage.setPassword(currentDateTimeString);
                    newMessage.setAlgorithm(algorithm);
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
            String passAux;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean decryptedSuccessful;
                messages.clear();
                String clickedUser=getIntent().getStringExtra("clickedUser");
                String currentUser=getIntent().getStringExtra("currentUser");
                if(clickedUser.compareTo(currentUser)>0)
                    passAux+=currentUser+clickedUser;
                else
                    passAux+=clickedUser+currentUser;

                for( DataSnapshot mSnapshot: dataSnapshot.child("Messages").getChildren()){
                    Message text=new Message(mSnapshot.getValue(Message.class));
                    if(text.getText()!=null&&text.getReceiver()!=null&&text.getSender()!=null)
                    if((text.getReceiver().equals(clickedUser)&& text.getSender().equals(currentUser))||
                            (
                        text.getSender().equals(clickedUser)&& text.getReceiver().equals(currentUser)
                            )) {
                        try {
                            if(text.getAlgorithm()!=null)
                                alg=text.getAlgorithm();
                            text.setText(Encrypt.decrypts(text.getText(),text.getPassword(),alg));
                            decryptedSuccessful=true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            decryptedSuccessful=false;
                        }
                        if(decryptedSuccessful)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_encryption,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        int id=item.getItemId();
        switch (id)
        {
            case R.id.AES:
                Toast.makeText(getApplicationContext(),"AES",Toast.LENGTH_SHORT).show();
                algorithm="AES";
                break;

            case R.id.BLOWFISH:
                Toast.makeText(getApplicationContext(),"Blowfish",Toast.LENGTH_SHORT).show();
                algorithm="BLOWFISH";
                break;

            case R.id.ARC4:
                Toast.makeText(getApplicationContext(),"ARC4",Toast.LENGTH_SHORT).show();
                algorithm="ARC4";
                break;
        }
        return true;
    }
}
