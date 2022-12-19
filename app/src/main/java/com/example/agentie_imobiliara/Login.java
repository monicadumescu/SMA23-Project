package com.example.agentie_imobiliara;

import static com.example.agentie_imobiliara.model.User.encodePassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        EditText password = (EditText) findViewById(R.id.editTextNumberPassword2);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button gotoRegister = (Button) findViewById(R.id.buttonback);
        FirebaseAuth authAction=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddress.getText().toString();
                String pass = password.getText().toString();
                if(emailAddress.getText().toString().equals("") || password.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please complete all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    authAction.signInWithEmailAndPassword(email, encodePassword(email, pass)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                openMainPage();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),""+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

    }

    public  void openMainPage() {
        startActivity(new Intent(this, MainPage.class));
    }

    public  void openRegister() {
        startActivity(new Intent(this, Register.class));
    }
}