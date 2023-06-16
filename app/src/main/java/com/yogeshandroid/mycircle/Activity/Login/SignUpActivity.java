package com.yogeshandroid.mycircle.Activity.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
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
                        String id = task.getResult().getUser().getUid();

                        User user = new User("", name, email, password,id, "","");
                        database.getReference().child("Users").child(id).setValue(user);
                        binding.emailEt.setText("");
                        binding.usernameEt.setText("");
                        binding.passwordEt.setText("");
                        Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        binding.progressCard.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.clickForSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LogIn.class)));
    }
}