package com.example.user.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private Button Login;
    private Button Register;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Username=(EditText)findViewById(R.id.editTextUsername);
        Password=(EditText)findViewById(R.id.editTextPassword);
        Login=(Button)findViewById(R.id.buttonLogin);
        Register=(Button)findViewById(R.id.buttonRegister);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Username.getText().toString().length()<4||Password.getText().toString().length()<6)
                    Toast.makeText(MainActivity.this,"Login failed, please try again.", Toast.LENGTH_SHORT).show();
                else
                    validate(Username.getText().toString(),Password.getText().toString());
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Username.getText().toString().length()>4&&Password.getText().toString().length()>6) {
                    mAuth.createUserWithEmailAndPassword(Username.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                                String id=mDatabase.push().getKey();
                                mDatabase.child("Users").child(id).setValue(Username.getText().toString());
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));

                            } else {
                                Toast.makeText(MainActivity.this, "Could not register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(MainActivity.this,"Please enter valid content!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void validate(String username, String password){
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                }else{
                    Toast.makeText(MainActivity.this,"Login failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
