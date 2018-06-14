package com.example.user.chatapp.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mUsername =findViewById(R.id.edit_text_username);
        mPassword =findViewById(R.id.edit_text_password);
        Button login = findViewById(R.id.button_login);
        Button register = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null){
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUsername.getText().toString().length()<4|| mPassword.getText().toString().length()<6)
                    Toast.makeText(LoginActivity.this,"Login failed, please try again.", Toast.LENGTH_SHORT).show();
                else
                    validate(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUsername.getText().toString().length()>4&& mPassword.getText().toString().length()>6) {
                    mAuth.createUserWithEmailAndPassword(mUsername.getText().toString(), mPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
                                String id=mDatabase.push().getKey();
                                mDatabase.child("Users").child(id).setValue(mUsername.getText().toString());

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            } else {
                                Toast.makeText(LoginActivity.this, "Could not register!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(LoginActivity.this,"Please enter valid content!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void validate(String username, String password){
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this,"Login failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
