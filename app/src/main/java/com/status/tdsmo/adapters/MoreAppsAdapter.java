package com.status.tdsmo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.status.tdsmo.R;
import com.status.tdsmo.models.Image;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * This app is developed by Sagar Khawse
 *
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 *
 * Date : - 6 march 2020
 */
public class MoreAppsAdapter extends RecyclerView.Adapter<MoreAppsAdapter.ImageViewHolder> {


    Context mContext;
    List<Image> mList;

    public MoreAppsAdapter(Context context, List<Image> list) {
        mContext = context;
        mList = list;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final Image data = mList.get(position);


        holder.title.setText(data.getTitle());
        try {
            Glide.with(mContext).load(data.getImage())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(holder.img);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse(data.getUrl()));
                mContext.startActivity(intent);
            }
        });
//
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title ;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);

        }
    }
}