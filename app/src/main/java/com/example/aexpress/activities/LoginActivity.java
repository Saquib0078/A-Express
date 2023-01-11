package com.example.aexpress.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.aexpress.R;
import com.example.aexpress.databinding.ActivityLoginBinding;
import com.example.aexpress.utils.SharedObjects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    Button login, createNewBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("please wait while logging in...");
        auth=FirebaseAuth.getInstance();

        binding.llCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = binding.edtEmail.getText().toString();
                pass = binding.edtPassword.getText().toString();
                if (binding.edtPassword.getText().toString().length() < 6) {
                    binding.edtPassword.setError("password should contain 6 character");
                    binding.edtPassword.requestFocus();
                    if (binding.edtEmail.getText().toString().length() < 6) {
                        binding.edtEmail.setError("please enter valid email");
                        binding.edtEmail.requestFocus();
                        if (!email.equals("") && binding.edtPassword.getText().toString().length() >= 6 &&
                                !binding.edtPassword.getText().toString().trim().equals("")) {
                        }
                    }

                }
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        if (auth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }




    }

    }
