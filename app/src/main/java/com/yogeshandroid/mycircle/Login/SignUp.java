package com.yogeshandroid.mycircle.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.signUpBtn.setOnClickListener(v -> {

            String name = binding.usernameEt.getText().toString();
            String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();
            if (name.isEmpty()) {
                binding.usernameEt.setError("enter Username");
                binding.usernameEt.requestFocus();
            } else if (email.isEmpty()) {
                binding.emailEt.setError("enter Email");
                binding.emailEt.requestFocus();
            } else if (password.isEmpty()) {
                binding.passwordEt.setError("enter Password");
                binding.passwordEt.requestFocus();
            } else {
                binding.progressCard.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressCard.setVisibility(View.GONE);
                        User user = new User("", name, email, password, "", "");
                        String id = task.getResult().getUser().getUid();
                        database.getReference().child("Users").child(id).setValue(user);
                        Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        binding.progressCard.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.clickForSignIn.setOnClickListener(v -> startActivity(new Intent(SignUp.this, LogIn.class)));
    }
}