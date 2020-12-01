package com.status.tdsmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.status.tdsmo.adapters.SwipeableAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public class ViewDpImageActivity extends AppCompatActivity {


    private ImageButton go_back;
    private String imgurl;
    private AdView adView;

    private RecyclerView recyclerView;
    private SwipeableAdapter adapter;
    private InterstitialAd mInterstitialAd;
    private LinearLayout adLayout;
    private DpStatusApi mService;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<Image> listOri;
    private Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dp_image);
        adLayout = findViewById(R.id.adview_linear_layout);
        mService = Common.getAPI();


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                        } else {
                            Toast.makeText(ViewDpImageActivity.this, "Permission denied! Some Functions may not work", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        try {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mInterstitialAd = new InterstitialAd(this);

            mInterstitialAd.setAdUnitId(Common.interstitial);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } catch (Exception e) {

        }


        if (Common.banner != null) {

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(Common.banner);
            adLayout.addView(adView);

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
//        mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
        if (Common.imageList != null) {

            String imageList = gson.toJson(Common.imageList);
            editor.putString("list", imageList);
            editor.apply();
        }

        String list = sharedPreferences.getString("list", null);

        Image[] data = gson.fromJson(list,
                Image[].class);

        listOri = Arrays.asList(data);
        listOri = new ArrayList<>(listOri);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        try {
            adapter = new SwipeableAdapter(this,listOri);
            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(Common.position);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    SnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(recyclerView);


        go_back = findViewById(R.id.go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        try {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                super.onBackPressed();
            } else super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
