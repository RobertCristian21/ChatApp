package com.example.user.chatapp.Activities;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.chatapp.Adapters.CustomMessageAdapter;
import com.example.user.chatapp.Encrypt;
import com.example.user.chatapp.Message;
import com.example.user.chatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    ListView ListMessages;
    private DatabaseReference dref;
    ArrayList<Message> messages=new ArrayList<>();
    ArrayList<String> keys=new ArrayList<>();
    private EditText messageTex;
    String algorithm="AES";//used in encrypting the messages when sent
    String alg;//used in decrypting messages when reading them
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    Message oldMessage=new Message();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        dref= FirebaseDatabase.getInstance().getReference();
        ListMessages=findViewById(R.id.list_view_allmessages);
        messageTex=findViewById(R.id.edit_text_newtext);
        Button Send=findViewById(R.id.button_send);
        TabLayout tabLayout = findViewById(R.id.tab_user);
        tabLayout.addTab(tabLayout.newTab().setText(getIntent().getStringExtra("clickedUser").toLowerCase()));

        ListMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Message aux=messages.get(position);
                oldMessage=aux;
                if(aux.getSender().equals(getIntent().getStringExtra("currentUser")))
                    aux.setSender(aux.getSender()+" ");
                else
                    if(aux.getReceiver().equals(getIntent().getStringExtra("currentUser")))
                        aux.setReceiver(aux.getReceiver()+" ");
                String encryptedMessage= null;
                try {
                    encryptedMessage = Encrypt.encrypts(aux.getText(),aux.getDate(),aux.getAlgorithm());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    aux.setText(encryptedMessage);
                    dref.child("Messages").child(keys.get(position)).setValue(aux);
                return false;

            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean encryptedSuccessful;
                Message newMessage=new Message();
                newMessage.setSender(getIntent().getStringExtra("currentUser"));

                newMessage.setReceiver(getIntent().getStringExtra("clickedUser"));

                try {
                    String encryptedMessage= Encrypt.encrypts(messageTex.getText().toString(),currentDateTimeString,algorithm);
                    newMessage.setText(encryptedMessage);
                    newMessage.setDate(currentDateTimeString);
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
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                messages.clear();
                keys.clear();
                String clickedUser=getIntent().getStringExtra("clickedUser");
                String currentUser=getIntent().getStringExtra("currentUser");

                for( DataSnapshot mSnapshot: dataSnapshot.child("Messages").getChildren()){
                    Message text=new Message(mSnapshot.getValue(Message.class));
                    if(text.getText()!=null&&text.getReceiver()!=null&&text.getSender()!=null) {

                        if(text.getSender().contains(" ")&&text.getReceiver().contains(" "))
                            dref.child("Messages").child(mSnapshot.getKey()).setValue(null);

                        else {
                            text = CheckMessage(text, currentUser, clickedUser);

                            if (text != null) {
                                messages.add(text);
                                keys.add(mSnapshot.getKey());
                            }
                        }
                    }
                }
                final ArrayAdapter<Message> adapter=new CustomMessageAdapter(MessageActivity.this,messages);
                ListMessages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Message CheckMessage(Message text, String currentUser, String clickedUser) {
        String Sender=text.getSender();
        String Receiver=text.getReceiver();
        if (text.getSender().contains(" ")) {
            Sender=Sender.substring(0,Sender.length()-1);
            if (text.getSender().substring(0, text.getSender().length() - 1).equals(currentUser))
                return null;
        }
        if (text.getReceiver().contains(" ")) {
            Receiver=Receiver.substring(0,Receiver.length()-1);
            if (text.getReceiver().substring(0, text.getReceiver().length() - 1).equals(currentUser))
                return null;
        }
        if((Sender.equals(currentUser)&&Receiver.equals(clickedUser))||(
                Receiver.equals(currentUser)&&Sender.equals(clickedUser))) {
            try {
                if (text.getAlgorithm() != null)
                    alg = text.getAlgorithm();
                text.setText(Encrypt.decrypts(text.getText(), text.getDate(), alg));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }
        return null;
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
