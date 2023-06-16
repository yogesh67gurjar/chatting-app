package com.yogeshandroid.mycircle.Fragment.Home.Tablayout;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yogeshandroid.mycircle.Activity.Profile.Profile;
import com.yogeshandroid.mycircle.Adapters.Status.StatusAdapter;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.databinding.FragmentStatusBinding;

import java.util.ArrayList;
import java.util.List;


public class Status extends Fragment {

    FragmentStatusBinding binding;
    FirebaseStorage storage;

    FirebaseDatabase database;
    List<User> usersJinseBaatHotiHe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        usersJinseBaatHotiHe = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.rvStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvStatus.setAdapter(new StatusAdapter(getContext(), null));

        binding.addStatusBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 123);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllStatus();
    }

    private void getAllStatus() {
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersJinseBaatHotiHe.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User single = snap.getValue(User.class);
                    single.setUserId(snap.getKey());
                    if (!FirebaseAuth.getInstance().getUid().equals(single.getUserId())) {

                        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + single.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    usersJinseBaatHotiHe.add(single);
                                }

//                                chatsAdapter.notifyDataSetChanged();

//                                if (userList.size() > 0) {
//                                    binding.RvMessage.setVisibility(View.VISIBLE);
//                                    binding.RvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    binding.RvMessage.setAdapter(chatsAdapter);
//                                    binding.ripple.setVisibility(View.GONE);
//                                } else {
//                                    binding.RvMessage.setVisibility(View.GONE);
//                                    binding.ripple.setVisibility(View.VISIBLE);
//                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
                Task<ListResult> reference = storage.getReference().child("status").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference single : listResult.getItems()) {
                            Log.e("jinse baat hoti", single.getName()+ "  " + single.getDownloadUrl().toString());

                        }
                    }
                });
//
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilePic").setValue(uri.toString());
//                                Toast.makeText(Profile.this, "profile picture successfully updated....", Toast.LENGTH_SHORT).show();
//                                binding.dp.setImageURI(urii);
//                            }
//                        });
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 123 && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "uploading , please wait....", Toast.LENGTH_SHORT).show();
            StorageReference reference = storage.getReference().child("status").child(FirebaseAuth.getInstance().getUid());

            reference.putFile(data.getData()).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    binding.addStatusImage.setImageURI(data.getData());
                    Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                }
            }));

        }
    }
}