package com.status.tdsmo.fragments.moreapps;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.status.tdsmo.R;
import com.status.tdsmo.adapters.MoreAppsAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
public class MoreAppsFragment extends Fragment {
private RecyclerView recyclerView;
    private List<Image> moreAppList;
private MoreAppsAdapter adapter;


    public MoreAppsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_apps, container, false);
    moreAppList = new ArrayList<>();
    recyclerView = view.findViewById(R.id.recycler_view);
        TextView textView = view.findViewById(R.id.text_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        DpStatusApi mService = Common.getAPI();

    mService.getMoreAppsList().enqueue(new Callback<DpDataResponse>() {
        @Override
        public void onResponse(@NonNull Call<DpDataResponse> call,@NonNull Response<DpDataResponse> response) {
            assert response.body() != null;
            if (!response.body().isError()){
                moreAppList = response.body().getRes();
                adapter = new MoreAppsAdapter(getContext(),moreAppList);
                recyclerView.setAdapter(adapter);


            }
        }

        @Override
        public void onFailure(@NonNull Call<DpDataResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "onFailure: "  + t.getMessage());

        }
    });
textView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "sagar.khawse@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Need Android Application");
        startActivity(Intent.createChooser(emailIntent, "Need Android Application"));
    }
});
        return view;
    }

}
