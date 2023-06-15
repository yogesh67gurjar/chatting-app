package com.yogeshandroid.mycircle.Fragment.Home.Tablayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yogeshandroid.mycircle.Adapters.ChatsAdapter;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.databinding.FragmentMessageTabBinding;

import java.util.ArrayList;
import java.util.List;


public class MessageTab extends Fragment {

    FragmentMessageTabBinding binding;
    FirebaseDatabase database;
    List<User> userList;
    ChatsAdapter chatsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageTabBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(getContext(), userList);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User single = snap.getValue(User.class);
                    single.setUserId(snap.getKey());
                    if (!FirebaseAuth.getInstance().getUid().equals(single.getUserId()))
                    {
                        userList.add(single);
                    }

                }
                chatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        binding.RvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.RvMessage.setAdapter(chatsAdapter);


        return binding.getRoot();
    }
}