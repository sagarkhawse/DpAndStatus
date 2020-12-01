package com.status.tdsmo.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.status.tdsmo.R;
import com.status.tdsmo.adapters.CategoryStatus;
import com.status.tdsmo.adapters.HindiCategoryStatusAdapter;
import com.status.tdsmo.adapters.StatusListAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public class StatusFragment extends Fragment {
    private RecyclerView recyclerView;
    private StatusListAdapter adapter;
    private List<Image> statusList;
    private static final String TAG = "StatusFragment";
    TextView status;
    ImageView fab_copy, fab_share, fab_whatsapp, fab_instagram;
    InterstitialAd mInterstitialAd;
    ProgressBar progressBar;
    CardView root;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hindi, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        statusList = new ArrayList<>();
        DpStatusApi mService = Common.getAPI();
        status = view.findViewById(R.id.status);
        fab_copy = view.findViewById(R.id.fab_copy);
        fab_share = view.findViewById(R.id.fab_share);
        fab_whatsapp = view.findViewById(R.id.fab_whatsapp);
        fab_instagram = view.findViewById(R.id.fab_instagram);
        progressBar = view.findViewById(R.id.progress_bar);
        root = view.findViewById(R.id.root);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        try {
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId(Common.interstitial);
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.d(TAG, "onCreateView: interstitial ad "+e.getMessage());
        }


        mService.getAllStatus().enqueue(new Callback<DpDataResponse>() {
            @Override
            public void onResponse(@NonNull Call<DpDataResponse> call, @NonNull Response<DpDataResponse> response) {
                assert response.body() != null;
                if (!response.body().isError()) {
                    List<Image> list = response.body().getRes();

                    for (int i=0; i<list.size(); i++){
                        if (i % 20 == 0){
                            Log.d(TAG, "onResponse: view type 6 Admob native ad view");
                            statusList.add(new Image("adview").setViewType(6));
                        }else {
                            Log.d(TAG, "onResponse: status list");
                            statusList.add(new Image(list.get(i).getStatus()).setViewType(0));
                        }
                    }




                    status.setText(statusList.get(statusList.size() - 1).getStatus());
//                    Collections.shuffle(statusList);
                    adapter = new StatusListAdapter(getContext(), statusList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    root.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DpDataResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, status.getText().toString());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });

        fab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, status.getText().toString());
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });

        fab_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, status.getText().toString());
                shareIntent.setType("text/plain");
                shareIntent.setPackage("com.instagram.android");
                startActivity(Intent.createChooser(shareIntent, "Share app via"));
            }
        });


        fab_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {



                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", status.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onClick: "+e.getMessage());
                }

            }
        });


        return view;
    }

}
