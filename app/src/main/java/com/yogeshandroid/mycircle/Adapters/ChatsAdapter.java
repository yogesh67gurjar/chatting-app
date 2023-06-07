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

import com.yogeshandroid.mycircle.InsideChat;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    Context context;

    public ChatsAdapter(Context context) {
        this.context = context;
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
        holder.cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, InsideChat.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
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
