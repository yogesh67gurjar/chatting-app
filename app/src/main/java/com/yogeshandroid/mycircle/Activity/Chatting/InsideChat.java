package com.yogeshandroid.mycircle.Activity.Chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yogeshandroid.mycircle.Adapters.InsideChat.InsideChatAdapter;
import com.yogeshandroid.mycircle.Modal.Message.MessageModel;
import com.yogeshandroid.mycircle.R;
import com.yogeshandroid.mycircle.databinding.ActivityInsideChatBinding;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsideChat extends AppCompatActivity {
    ActivityInsideChatBinding binding;
    FirebaseAuth auth;

    String senderId;
    String receiverId;

    List<MessageModel> messageModelList;
    InsideChatAdapter insideChatAdapter;
    FirebaseDatabase database;

    String senderRoom;
    String receiverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsideChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        messageModelList = new ArrayList<>();
        insideChatAdapter = new InsideChatAdapter(messageModelList, InsideChat.this);

        binding.nameInsideChat.setText(getIntent().getStringExtra("name"));
        Glide.with(InsideChat.this).load(getIntent().getStringExtra("dp")).placeholder(R.drawable.smiling).into(binding.dpInsideChat);

        receiverId = getIntent().getStringExtra("uId");
        senderId = auth.getUid();

        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;

        Log.e("SENDERID", senderId);
        Log.e("RECEIVERID", receiverId);

        binding.chattingRv.setAdapter(insideChatAdapter);
        binding.chattingRv.setLayoutManager(new LinearLayoutManager(InsideChat.this));

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModel model = snapshot1.getValue(MessageModel.class);
                    messageModelList.add(model);
                }
                insideChatAdapter.notifyDataSetChanged();
                binding.chattingRv.scrollToPosition(messageModelList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.text.getText().toString();
                final MessageModel model = new MessageModel(senderId, msg);
                model.setTimeStamp(new Date().getTime());
                database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.text.setText("");
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {}
                        });
                    }
                });
            }
        });
    }
}