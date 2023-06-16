package com.yogeshandroid.mycircle.Adapters.Status;

import android.content.Context;
import android.net.Uri;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yogeshandroid.mycircle.Modal.Status.StatusUpdate;
import com.yogeshandroid.mycircle.R;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    Context context;
    List<StatusUpdate> statusUpdateList;

    public StatusAdapter(Context context, List<StatusUpdate> statusUpdateList) {
        this.context = context;
        this.statusUpdateList = statusUpdateList;
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

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        CircleImageView statusDp;
        TextView statusName;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusDp = itemView.findViewById(R.id.statusDp);
            statusName = itemView.findViewById(R.id.statusName);

        }
    }
}
