package com.yogeshandroid.mycircle.Activity.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityProfileBinding;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    ActivityProfileBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(Profile.this).load(user.getProfilePic()).placeholder(R.drawable.smiling).into(binding.dp);
                binding.name.setText(user.getUserName());
                binding.status.setText(user.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        bauth.getCurrentUser().getDisplayName());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/**");
                startActivityForResult(intent, 123);
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = binding.status.getText().toString();
                String name = binding.name.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userName", name);
                hashMap.put("status", status);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Profile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK && data.getData() != null) {
            Uri urii = data.getData();

            StorageReference reference = storage.getReference().child("dp").child(FirebaseAuth.getInstance().getUid());

            reference.putFile(urii).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilePic").setValue(uri.toString());
                            Toast.makeText(Profile.this, "profile picture successfully updated....", Toast.LENGTH_SHORT).show();
                            binding.dp.setImageURI(urii);
                        }
                    });
                }
            });
        }
    }
}