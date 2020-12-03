package com.status.tdsmo.fragments.statusFragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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
import com.status.tdsmo.MainActivity;
import com.status.tdsmo.R;
import com.status.tdsmo.adapters.CategoryStatus;
import com.status.tdsmo.adapters.HindiCategoryStatusAdapter;
import com.status.tdsmo.adapters.StatusListAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.fragments.statusFragment.adapter.StatusAdapter;
import com.status.tdsmo.fragments.statusFragment.viewmodel.StatusNav;
import com.status.tdsmo.fragments.statusFragment.viewmodel.StatusViewModel;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;
import com.status.tdsmo.utils.PaginationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.status.tdsmo.utils.PaginationListener.PAGE_START;

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public class StatusFragment extends Fragment implements StatusNav {
    private RecyclerView recyclerView;
    private StatusListAdapter adapter;
    private List<Image> statusList = new ArrayList<>();
    private StatusViewModel viewModel;
    private StatusAdapter statusAdapter;
    private static final String TAG = "StatusFragment";
    TextView status;
    ImageView fab_copy, fab_share, fab_whatsapp, fab_instagram;
    InterstitialAd mInterstitialAd;
    ProgressBar progressBar;
    CardView root;
    DpStatusApi mService;
    PagedList<Image> getStatusList;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hindi, container, false);
        viewModel = ViewModelProviders.of(this).get(StatusViewModel.class);
        viewModel.LoadPaging(this);
        recyclerView = view.findViewById(R.id.recycler_view);
        mService = Common.getAPI();
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
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreateView: interstitial ad " + e.getMessage());
        }
        statusAdapter=new StatusAdapter(getContext());
        recyclerView.setAdapter(statusAdapter);
        viewModel.getStatusList().observe((MainActivity)getContext(), new Observer<PagedList<Image>>() {
            @Override
            public void onChanged(PagedList<Image> list) {
                getStatusList=list;
                statusAdapter.submitList(getStatusList);
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
                    Log.d(TAG, "onClick: " + e.getMessage());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setProgress(final boolean b) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(b){
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    root.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void setMessage(final String server_not_responding) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), server_not_responding, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setListEndStatus(Image image) {
        status.setText(image.getStatus());
    }
}
