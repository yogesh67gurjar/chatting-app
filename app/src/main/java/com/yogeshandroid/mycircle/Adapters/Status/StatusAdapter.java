package com.yogeshandroid.mycircle.Adapters.Status;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yogeshandroid.mycircle.Modal.Status.StatusUpdate;
import com.yogeshandroid.mycircle.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    Context context;
    List<String> statusOn;
    List<String> naamOn;

    public StatusAdapter(Context context, List<String> statusOn, List<String> naamOn) {
        this.context = context;
        this.statusOn = statusOn;
        this.naamOn = naamOn;
    }

    @NonNull
    @Override
    public StatusAdapter.StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.StatusViewHolder holder, int position) {
        holder.statusName.setText(naamOn.get(position));
        String image = statusOn.get(position);
        Glide.with(context).load(image).placeholder(R.drawable.smiling).into(holder.statusDp);

        holder.statusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.getWindow().setBackgroundDrawableResource(R.color.trans);
                dialog.setContentView(R.layout.dialog_status);
                ImageView dialogStatus = dialog.findViewById(R.id.dialogStatus);
                Glide.with(context).load(image).placeholder(R.drawable.smiling).into(dialogStatus);

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return naamOn.size();
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        CircleImageView statusDp;
        TextView statusName;
        LinearLayout statusCard;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusDp = itemView.findViewById(R.id.statusDp);
            statusName = itemView.findViewById(R.id.statusName);
            statusCard = itemView.findViewById(R.id.statusCard);
        }
    }
}
