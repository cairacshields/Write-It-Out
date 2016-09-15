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

public class SignupActivity extends AppCompatActivity {

    EditText uEmail, uPassword;
    Button uSignUp;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        uEmail = (EditText)findViewById(R.id.emailField);
        uPassword = (EditText)findViewById(R.id.passwordField);
        uSignUp = (Button)findViewById(R.id.signUpButton);

        uSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = uEmail.getText().toString();
                String password = uPassword.getText().toString();

                email = email.trim();
                password = password.trim();

                if(password.length() <= 5){
                    uPassword.setError("Password must be at least 6 characters!");
                }else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(SignupActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                        builder.setTitle("Error with Sign Up!")
                                                .setMessage(task.getException().getMessage())
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
