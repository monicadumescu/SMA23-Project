package com.example.agentie_imobiliara;

import static com.example.agentie_imobiliara.model.User.encodePassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import  com.example.agentie_imobiliara.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText emailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText) findViewById(R.id.editTextNumberPassword);
        EditText repeatpassword = (EditText) findViewById(R.id.editTextTextPasswordRepeat);
        Button addUserB = (Button) findViewById(R.id.addUserB);
            Button gotoLogin = (Button) findViewById(R.id.buttonlog);
        FirebaseAuth authAction = FirebaseAuth.getInstance();

        addUserB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailAddress.getText().toString().equals("") || password.getText().toString().equals("") || repeatpassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory!", Toast.LENGTH_SHORT).show();
                } else if (!Objects.equals(password.getText().toString(), repeatpassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords does not match!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    authAction.createUserWithEmailAndPassword(emailAddress.getText().toString(), encodePassword(emailAddress.getText().toString(),password.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                openLogin();
                                Toast.makeText(getApplicationContext(), "You were registered successfully!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),""+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });
    }

    public  void openLogin()
    {
        startActivity(new Intent(this,Login.class));
    }
}