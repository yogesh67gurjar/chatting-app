package com.yogeshandroid.mycircle.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.yogeshandroid.mycircle.Activity.MainActivity;
import com.yogeshandroid.mycircle.Modal.Message.MessageModel;
import com.yogeshandroid.mycircle.Modal.User;
import com.yogeshandroid.mycircle.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;

    List<User> userList;

    public SearchAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_search, parent, false);
        return new SearchViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        User singleUnit = userList.get(position);
        holder.name.setText(singleUnit.getUserName());
        Glide.with(context).load(singleUnit.getProfilePic()).placeholder(R.drawable.smiling).into(holder.dp);

        holder.sayHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MessageModel model = new MessageModel(FirebaseAuth.getInstance().getUid(), "hi");
                model.setTimeStamp(new Date().getTime());
                String senderRoom = FirebaseAuth.getInstance().getUid() + singleUnit.getUserId();
                String receiverRoom = singleUnit.getUserId() + FirebaseAuth.getInstance().getUid();
                FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "new conversation started with "+ singleUnit.getUserName(), Toast.LENGTH_SHORT).show();
                                ((MainActivity) context).hiBolDiya();
                            }
                        });
                    }
                });
            }
        });
    }

    public void filterList(ArrayList<User> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        userList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        CircleImageView dp;
        AppCompatButton sayHi;
        TextView name;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            dp = itemView.findViewById(R.id.dpSearch);
            sayHi = itemView.findViewById(R.id.sayHiButton);
            name = itemView.findViewById(R.id.nameSearch);
        }
    }
}
