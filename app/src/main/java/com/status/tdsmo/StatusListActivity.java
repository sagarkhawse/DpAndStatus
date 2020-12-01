package com.status.tdsmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public class StatusListActivity extends AppCompatActivity {
    private StatusListAdapter adapter;
    private List<Image> statusList;
    private DpStatusApi mService;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(StatusListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mService = Common.getAPI();
        statusList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String category = bundle.getString("category");
        String hindi_category = bundle.getString("hindi_category");

        if (category != null) {
            Log.d("NULLLLL", "onCreate: not null");
            mService.getStatusList(category).enqueue(new Callback<DpDataResponse>() {
                @Override
                public void onResponse(Call<DpDataResponse> call, Response<DpDataResponse> response) {
                    if (!response.body().isError()) {
                        statusList = response.body().getRes();
                        Collections.shuffle(statusList);
                        adapter = new StatusListAdapter(StatusListActivity.this, statusList);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<DpDataResponse> call, Throwable t) {
                    Toast.makeText(StatusListActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("NULLLLL", "onCreate:  null");

            mService.getHindiStatusList(hindi_category).enqueue(new Callback<DpDataResponse>() {
                @Override
                public void onResponse(Call<DpDataResponse> call, Response<DpDataResponse> response) {
                    if (!response.body().isError()) {
                        statusList = response.body().getRes();
                        Collections.shuffle(statusList);
                        adapter = new StatusListAdapter(StatusListActivity.this, statusList);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<DpDataResponse> call, Throwable t) {
                    Toast.makeText(StatusListActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
