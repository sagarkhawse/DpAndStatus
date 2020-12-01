package com.status.tdsmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.status.tdsmo.adapters.DpAdapter;
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
 *
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 *
 * Date : - 6 march 2020
 */
public class ViewCategoryDpActivity extends AppCompatActivity {
private DpAdapter adapter ;
private List<Image> imageList;
private RecyclerView recyclerView;
private DpStatusApi mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category_dp);

        imageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mService = Common.getAPI();

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        mService.getDpByCategory(title).enqueue(new Callback<DpDataResponse>() {
            @Override
            public void onResponse(Call<DpDataResponse> call, Response<DpDataResponse> response) {
                if (!response.body().isError()){

                    imageList = response.body().getRes();
                    Collections.shuffle(imageList);
                    adapter = new DpAdapter(ViewCategoryDpActivity.this,imageList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<DpDataResponse> call, Throwable t) {

            }
        });
    }
}
