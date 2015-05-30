package com.strangerchat.strangerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView message;
    public ImageView icon;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.txt1);
        icon = (ImageView) itemView.findViewById(R.id.pic);
        message = (TextView) itemView.findViewById(R.id.message);
    }
}
