package com.example.aexpress.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aexpress.R;
import com.example.aexpress.databinding.ActivityRegisterBinding;
import com.example.aexpress.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        // below line is used to get reference for our database.
        databaseReference = database.getReference("CreateUser");

       User user = new User();


        // below line is used to get reference for our database.


        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);

        binding.llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, email, password, phone;

                email = binding.edtEmail.getText().toString();
                password = binding.edtPassword.getText().toString();
                name = binding.edtName.getText().toString();
                phone = binding.edtPhone.getText().toString();

                if (binding.edtPassword.getText().toString().length() < 6) {
                    binding.edtPassword.setError("password should contain 6 character");
                    binding.edtPassword.requestFocus();
                }
                if (!email.equals("") && binding.edtPassword.getText().toString().length() >= 6 &&
                        !binding.edtPassword.getText().toString().trim().equals("")) {
                }
                if (email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "please enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String key = databaseReference.push().getKey();


                final User user = new User(name, email, password, phone);


                dialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = task.getResult().getUser().getUid();

//                            databaseReference.child(key);


                            user.setName(name);
                            user.setEmail(email);
                            user.setPhone(phone);
                            user.setPassword(password);
                            user.setKey(key);
                            user.setUid(FirebaseAuth.getInstance().getUid());
                            Log.e("Log",user.getUid());
                            Log.e("Log",user.getKey());
                            assert key != null;

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("name",user.getName());
                            hashMap.put("email",user.getEmail());
                            hashMap.put("password",user.getPassword());
                            hashMap.put("phone",user.getPhone());
                            hashMap.put("key",user.getKey());
                            database.getReference("NewUsers").child(user.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finishAffinity();
                                    }
                                }
                            });

                            databaseReference.child(key).setValue(user);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    databaseReference.setValue(user);
                                    dialog.dismiss();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });



                        } else {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}