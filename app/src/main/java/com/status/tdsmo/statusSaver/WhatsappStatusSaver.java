package com.status.tdsmo.statusSaver;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.status.tdsmo.R;
import com.status.tdsmo.statusSaver.adapters.PagerAdapter;
import com.status.tdsmo.statusSaver.utils.PermissionUtils;


public class WhatsappStatusSaver extends AppCompatActivity implements PermissionUtils.Callback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_saver_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new PermissionUtils(this, this, this).begin();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PermissionUtils.RQ_CODE_ASK_PERMISSION) {

            boolean isAllPermissionGranted = true;
            for (final int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllPermissionGranted = false;
                    break;
                }
            }

            if (isAllPermissionGranted) {
                onAllPermissionGranted();
            } else {
                onPermissionDenial();
            }
        }
    }

    @Override
    public void onAllPermissionGranted() {
        ViewPager vpStatuses = (ViewPager) findViewById(R.id.vpStatuses);
        TabLayout tabLayout = findViewById(R.id.tabs);
        vpStatuses.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(vpStatuses);
    }

    @Override
    public void onPermissionDenial() {
        Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        finish();
    }
}
