package com.yogeshandroid.mycircle.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yogeshandroid.mycircle.Activity.Chatting.InsideChat;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    Context context;
    List<User> userList;

    public ChatsAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_chat, parent, false);
        return new ChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatsViewHolder holder, int position) {

        User singleUnit = userList.get(position);

        Glide.with(context).load(singleUnit.getProfilePic()).placeholder(R.drawable.smiling).into(holder.dpChat);

        holder.cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InsideChat.class);
                intent.putExtra("name", singleUnit.getUserName());
                intent.putExtra("dp", singleUnit.getProfilePic());
                intent.putExtra("uId", singleUnit.getUserId());
                context.startActivity(intent);
            }
        });

        holder.nameChat.setText(singleUnit.getUserName());

//        sender room'FirebaseAuth.getInstance().getUid()+singleUnit.getUserId()
        // order by se apn ko last ki cheej mil jaegi
        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + singleUnit.getUserId()).orderByChild("timeStamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                        holder.lastMsgChat.setText(snapshot1.child("message").getValue().toString());
                        // same
                        holder.lastMsgChat.setText(snapshot1.child("message").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView dpChat;
        ImageView tickChat;
        TextView lastMsgChat, nameChat, timeChat;

        CardView cardChat;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            dpChat = itemView.findViewById(R.id.dpChat);
            tickChat = itemView.findViewById(R.id.tickChat);
            lastMsgChat = itemView.findViewById(R.id.lastMsgChat);
            nameChat = itemView.findViewById(R.id.nameChat);
            timeChat = itemView.findViewById(R.id.timeChat);
            cardChat = itemView.findViewById(R.id.cardChat);
        }
    }
}
