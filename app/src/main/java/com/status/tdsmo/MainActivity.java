package com.status.tdsmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.status.tdsmo.adapters.ViewPagerAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.fragments.CategoryFragment;
import com.status.tdsmo.fragments.EnglishFragment;

import com.status.tdsmo.fragments.HomeFragment;
import com.status.tdsmo.fragments.MoreAppsFragment;
import com.status.tdsmo.fragments.StatusFragment;
import com.status.tdsmo.fragments.TrendingFragment;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This app is developed by Sagar Khawse
 *
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 *
 * Date : - 6 march 2020
 */




public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private AdView adView;
    private ImageButton rating_img_btn, share_img_btn,privacy_img_btn;
    private ProgressDialog progressDialog;
    private DpStatusApi mService;
    private LinearLayout adLayout;
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adLayout = findViewById(R.id.adview_linear_layout);
        tabLayout = findViewById(R.id.tablayout_id);
        appBarLayout= findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.view_pager);
        rating_img_btn = findViewById(R.id.rating_img_btn);
        share_img_btn = findViewById(R.id.share_img_btn);
        privacy_img_btn = findViewById(R.id.privacy_img_btn);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mService = Common.getAPI();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast

                    }
                });
        Thread.setDefaultUncaughtExceptionHandler(new MyThreadUncaughtExceptionHandler());
        try {
            mService.getAdmobAds(1).enqueue(new Callback<Image>() {
                @Override
                public void onResponse(@NonNull Call<Image> call,@NonNull Response<Image> response) {
                    assert response.body() != null;
                    try {
                        Common.banner = response.body().getBanner();
                        Common.interstitial = response.body().getInterstitial();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    Log.d("ADMOBADS", "onResponse: "+Common.banner);
                    Log.d("ADMOBADS", "onResponse: "+Common.interstitial);
                    loadAds();
                }

                @Override
                public void onFailure(@NonNull Call<Image> call,@NonNull Throwable t) {

                }
            });


        }catch (NullPointerException e){
            e.printStackTrace();
        }





progressDialog = new ProgressDialog(this);
progressDialog.setCancelable(false);
progressDialog.setTitle("No Internet Connectivity found");
progressDialog.setMessage("please connect to internet");

if (!isNetworkAvailable()){
   progressDialog.show();
    }else {
    progressDialog.dismiss();
    }

   ViewPagerAdapter adapter =  new ViewPagerAdapter(getSupportFragmentManager(),0);
        adapter.addFragment(new StatusFragment(),"Status");
        adapter.addFragment(new HomeFragment(),"Latest");
        adapter.addFragment(new TrendingFragment(),"Trending");
        adapter.addFragment(new CategoryFragment(),"Category");
        adapter.addFragment(new EnglishFragment(),"Status Category");
        adapter.addFragment(new MoreAppsFragment(),"More Apps");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        share_img_btn.setOnClickListener(this);
        rating_img_btn.setOnClickListener(this);
        privacy_img_btn.setOnClickListener(this);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        adView = findViewById(R.id.adView1);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rating_img_btn:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.status.tdsmo"));
                startActivity(intent);
            break;
            case R.id.share_img_btn:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.status.tdsmo");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share app via"));
            break;
            case R.id.privacy_img_btn:
Intent intent1 = new Intent(MainActivity.this,PrivacyPolicyActivity.class);
startActivity(intent1);
             break;
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    private void loadAds(){

        if (Common.banner != null){
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(Common.banner);
            adLayout.addView(adView);



//        mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        if (Common.interstitial != null){
            try {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId(Common.interstitial);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }catch (Exception e){
                Toast.makeText(this, "Failed to load "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        try {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

            }
        });
        if (Common.interstitial != null){
            if (mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
                super.onBackPressed();
            }else {
                super.onBackPressed();

            }
        }else {
            super.onBackPressed();
        }

        }catch (Exception e){
         e.printStackTrace();
        }

    }
}
