package com.yogeshandroid.mycircle.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;

//    FirebaseAuth auth;
//    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        auth=FirebaseAuth.getInstance();
//        database=FirebaseDatabase.getInstance();

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog.show();
                String name=binding.usernameEt.getText().toString();
                String email=binding.emailEt.getText().toString();
                String password=binding.passwordEt.getText().toString();
                if(name.isEmpty())
                {
                    binding.usernameEt.setError("enter Username");
                    binding.usernameEt.requestFocus();
                }
                if(email.isEmpty())
                {
                    binding.emailEt.setError("enter Email");
                    binding.emailEt.requestFocus();
                }
                if(password.isEmpty())
                {
                    binding.passwordEt.setError("enter Password");
                    binding.passwordEt.requestFocus();
                }
                if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty())
                {
//                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            progressDialog.dismiss();
//                            if (task.isSuccessful())
//                            {
//                                Users users=new Users(name,email,password);
//                                String id=task.getResult().getUser().getUid();
//                                database.getReference().child("Users").child(id).setValue(users);
//                                Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            }
        });

        binding.alreadyhaveaccountTv.setOnClickListener(v -> startActivity(new Intent(SignUp.this,SignIn.class)));
    }
}