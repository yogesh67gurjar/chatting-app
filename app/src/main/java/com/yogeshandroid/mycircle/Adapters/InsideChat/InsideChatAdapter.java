package com.yogeshandroid.mycircle.Adapters.InsideChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.yogeshandroid.mycircle.Modal.Message.MessageModel;
import com.yogeshandroid.mycircle.R;

import java.util.List;

public class InsideChatAdapter extends RecyclerView.Adapter {
    List<MessageModel> messageModelList;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public InsideChatAdapter(List<MessageModel> messageModelList, Context context) {
        this.messageModelList = messageModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_sender,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_reciever,parent,false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel singleUnit=messageModelList.get(position);
        if(holder.getClass()==senderViewHolder.class)
        {
            ((senderViewHolder)holder).senderMsg.setText(singleUnit.getMessage());
        }
        else {
            ((receiverViewHolder)holder).receiverMsg.setText(singleUnit.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(messageModelList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.txtReceiver);
            receiverTime = itemView.findViewById(R.id.txtReceiverTime);
        }
    }

    public class senderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.txtSender);
            senderTime = itemView.findViewById(R.id.txtSenderTime);
        }
    }
}
