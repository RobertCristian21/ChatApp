package com.example.user.chatapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private Button SingOut;
    private FirebaseAuth mAuth;
    ListView ListView;
    private DatabaseReference dref;
    ArrayList<String>Users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView=findViewById(R.id.list_view_allusers);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(HomeActivity.this,MessageActivity.class);

                intent.putExtra("clickedUser",Users.get(position));
                intent.putExtra("currentUser",mAuth.getCurrentUser().getEmail());
                Toast.makeText(HomeActivity.this,Users.get(position),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        dref= FirebaseDatabase.getInstance().getReference();

        SingOut =  findViewById(R.id.button_signout);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            finish();
            startActivity(new Intent(HomeActivity.this,StartActivity.class));
        }

        SingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,StartActivity.class));
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users.clear();
                for( DataSnapshot userSnapshot: dataSnapshot.child("Users").getChildren()){
                    String user=userSnapshot.getValue(String.class);
                    if(!user.equals(mAuth.getCurrentUser().getEmail()))
                        Users.add(user);
                }
                final ArrayAdapter<String> adapter=new ArrayAdapter<>(HomeActivity.this,android.R.layout.simple_dropdown_item_1line,Users);
                ListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
