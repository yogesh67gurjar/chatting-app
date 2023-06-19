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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yogeshandroid.mycircle.Activity.Profile.Profile;
import com.yogeshandroid.mycircle.Adapters.Status.StatusAdapter;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.FragmentStatusBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Status extends Fragment {

    FragmentStatusBinding binding;
    FirebaseStorage storage;

    FirebaseDatabase database;
    List<User> usersJinseBaatHotiHe;

    StatusAdapter statusAdapter;

    List<User> userList;
    List<String> statusOn;
    List<String> naamOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        usersJinseBaatHotiHe = new ArrayList<>();

        userList = new ArrayList<>();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        statusOn = new ArrayList<>();
        naamOn = new ArrayList<>();

        statusAdapter = new StatusAdapter(getContext(), statusOn, naamOn);
        binding.rvStatus.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvStatus.setAdapter(statusAdapter);

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
        statusOn.clear();

        storage.getReference().child("status").child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(getContext()).load(task.getResult().toString()).placeholder(R.drawable.smiling).into(binding.addStatusImage);
                }
            }
        });

//        storage.getReference().child("status").listAll().addOnSuccessListener(listResult -> {
//            for (StorageReference single : listResult.getItems()) {
//
//                if (!single.getDownloadUrl().getResult().toString().equals(FirebaseAuth.getInstance().getUid())) {
//                    Log.e("all status", single.getName() + "  " + single.getDownloadUrl().getResult().toString());
//                }
//            }
//        });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                statusOn.clear();
                naamOn.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User single = snap.getValue(User.class);
                    single.setUserId(snap.getKey());
                    if (!FirebaseAuth.getInstance().getUid().equals(single.getUserId())) {
                        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + single.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    userList.add(single);
                                }

                                // yaha pr apne paas saare user he
                                for (User singleUnit : userList) {
                                    Log.e("user he jisse baat hoti he", singleUnit.getUserName());

                                    storage.getReference().child("status").child(singleUnit.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("user ka status he", task.getResult().toString());
                                                if (!statusOn.contains(task.getResult().toString()) && !naamOn.contains(singleUnit.getUserName())) {
                                                    statusOn.add(task.getResult().toString());
                                                    naamOn.add(singleUnit.getUserName());


                                                    statusAdapter.notifyDataSetChanged();

                                                    Log.e("size he ", statusOn.size() + "   " + naamOn.size());
                                                    if (statusOn.size() > 0 && naamOn.size() > 0) {

                                                        binding.nothing.setVisibility(View.GONE);
                                                        binding.rvStatus.setVisibility(View.VISIBLE);
                                                    } else {
                                                        binding.nothing.setVisibility(View.VISIBLE);
                                                        binding.rvStatus.setVisibility(View.GONE);
                                                    }

                                                }
                                            }
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

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