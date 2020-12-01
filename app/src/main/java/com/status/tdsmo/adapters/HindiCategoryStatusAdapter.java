package com.status.tdsmo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.status.tdsmo.R;
import com.status.tdsmo.StatusListActivity;
import com.status.tdsmo.models.Image;

import java.util.List;
/**
 * This app is developed by Sagar Khawse
 *
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 *
 * Date : - 6 march 2020
 */

public class HindiCategoryStatusAdapter extends RecyclerView.Adapter<HindiCategoryStatusAdapter.TextViewHolder>{
    Context mContext;
    List<Image> mList;

    public HindiCategoryStatusAdapter(Context context, List<Image> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_status,parent,false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        final Image data = mList.get(position);
        holder.title.setText(data.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StatusListActivity.class);
                intent.putExtra("hindi_category",data.getTitle());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class TextViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_status);
        }
    }
}

