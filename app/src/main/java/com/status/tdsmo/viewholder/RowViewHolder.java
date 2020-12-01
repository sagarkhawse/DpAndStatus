package com.status.tdsmo.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.status.tdsmo.interfacee.RecyclerViewClickListener;

public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerViewClickListener mListener;

    RowViewHolder(View v, RecyclerViewClickListener listener) {
        super(v);
        mListener = listener;
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }
}