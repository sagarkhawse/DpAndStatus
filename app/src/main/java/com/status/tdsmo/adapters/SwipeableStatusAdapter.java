package com.status.tdsmo.adapters;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.status.tdsmo.R;
import com.status.tdsmo.common.Common;
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
public class SwipeableStatusAdapter extends RecyclerView.Adapter<SwipeableStatusAdapter.TextViewholder>{
    Context mContext;
    List<Image> mList;


    private     boolean isShare = false;
    private boolean isShareWhatsapp = false;
    private boolean isShareFb = false;
    private boolean isShareInstagram = false;
    private ProgressDialog progressDialog;

    private InterstitialAd mInterstitialAd;

    public SwipeableStatusAdapter(Context mContext, List<Image> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public TextViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.status_swipe_item,parent,false);
        progressDialog = new ProgressDialog(mContext);
        if (Common.interstitial!=null){
            MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mInterstitialAd = new InterstitialAd(mContext);

            mInterstitialAd.setAdUnitId(Common.interstitial);
        }





        return new TextViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TextViewholder holder, int position) {
        final Image data = mList.get(position);


        if (position % 2 == 0){
holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorOne));
        }

        if (position % 4 == 0){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorTwo));
        }

        if (position % 6 == 0){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorThree));
        }

        if (position % 8 == 0){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorFour));
        }

        if (position % 10 == 0){
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorFive));
        }
holder.status_tv.setText(data.getStatus());

        holder.fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.status_tv.getText().toString());
                shareIntent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });

        holder.fab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.status_tv.getText().toString());
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.whatsapp");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });

        holder.fab_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.status_tv.getText().toString());
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.instagram.android");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });


        holder.fab_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Common.interstitial!=null){
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }}
                    ClipboardManager clipboard =(ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text",  holder.status_tv.getText().toString());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(mContext, "Text Copied", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
         return mList.size();
    }


    public class TextViewholder extends RecyclerView.ViewHolder{
        ImageView fab_copy, fab_share, fab_whatsapp,fab_instagram;
      TextView status_tv;
      CardView cardView;
        public TextViewholder(@NonNull View itemView) {
            super(itemView);

            status_tv = itemView.findViewById(R.id.status_tv);
            fab_copy = itemView.findViewById(R.id.fab_copy);
            fab_share = itemView.findViewById(R.id.fab_share);
            fab_whatsapp = itemView.findViewById(R.id.fab_whatsapp);
            cardView = itemView.findViewById(R.id.card_view);
            fab_instagram = itemView.findViewById(R.id.fab_instagram);
        }
    }


}
