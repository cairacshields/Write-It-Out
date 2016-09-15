package com.example.paul.writeitout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText uEmail, uPassword;
    Button uLogin;
    TextView uSignUp;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //First, we initialize firebase
        mFirebaseAuth =FirebaseAuth.getInstance();

        uEmail = (EditText)findViewById(R.id.emailField);
        uPassword = (EditText)findViewById(R.id.passwordField);
        uLogin = (Button)findViewById(R.id.loginButton);
        uSignUp = (TextView)findViewById(R.id.signUpText);

        //Set an onClick listener for the Sign Up text that will move the new user to
        //The sign up activity.
        uSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = uEmail.getText().toString();
                String password = uPassword.getText().toString();

                email = email.trim();
                password = password.trim();

                //We can check to make sure that the fields are not empty before proceeding
                if(email.isEmpty() || password.isEmpty()){
                    uEmail.setError("One or more fields are empty!");
                }else{
                    mFirebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }else{
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("Login Failed")
                                                .setPositiveButton("OK", null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
