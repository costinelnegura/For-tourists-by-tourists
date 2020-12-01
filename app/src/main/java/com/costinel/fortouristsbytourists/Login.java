package com.costinel.fortouristsbytourists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    // creating the impostors for the activity layout;
    Button login, cancel, register;
    EditText email_edit_text, password_edit_text;

    // creating a Firebase auth reference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // linking the impostors to the activity layout;
        login = findViewById(R.id.bt_login);
        cancel = findViewById(R.id.bt_cancel);
        register = findViewById(R.id.bt_register_2);
        email_edit_text = findViewById(R.id.txt_email_address);
        password_edit_text = findViewById(R.id.txt_password);
        auth = FirebaseAuth.getInstance();

        // creating a onClickListener for the login button impostor;
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // creating 2 string variables to save the input from the user from the
                // activity layout;
                String email =  email_edit_text.getText().toString().trim();
                String password = password_edit_text.getText().toString().trim();

                // checking if the user typed both email or password;
                // prompting if something is not typed;
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter email and password",
                            Toast.LENGTH_LONG).show();
                }

                // checking if the email matched what is saved on the database;
                // prompting if the email address is invalid
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "Email not valid",
                            Toast.LENGTH_LONG).show();
                }

                // checking is the password is 6 characters long minimum;
                // prompting if the password length is less than 6 characters;
                if(password.length() < 6){
                    Toast.makeText(getApplicationContext(),
                            "Password length is minimum 6 characters!",
                            Toast.LENGTH_LONG).show();
                }

                // creating the method that enables the user to login if every check was passed;
                // if successful, onComplete has an Intent that redirects the user to the main
                // page that allows the user to create attractions;
                // otherwise the process failed and the user is prompted to try again;
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(Login.this,
                                    MainActivity_logged_in.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to login!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // set an onClickListener to the cancel button impostor that redirects the user to the
        // main activity layout for non logged in users;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(Login.this, MainActivity.class);
                startActivity(cancel);
            }
        });

        // set an onClickListener to the register impostor button to allow the user to create
        // an account if non exists, redirecting to the register activity layout;;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Register.class);
                startActivity(register);
            }
        });
    }
}