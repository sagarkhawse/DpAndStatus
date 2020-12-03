package com.status.tdsmo.fragments.trending;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.status.tdsmo.R;
import com.status.tdsmo.adapters.DpAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import java.util.Collections;
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
public class TrendingFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Image> imageList;
    private DpStatusApi mService;
    private DpAdapter adapter;

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mService = Common.getAPI();
   return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    mService.getTrendingList().enqueue(new Callback<DpDataResponse>() {
        @Override
        public void onResponse(@NonNull Call<DpDataResponse> call,@NonNull Response<DpDataResponse> response) {
            assert response.body() != null;
            if (!response.body().isError()){
                imageList = response.body().getRes();
                Collections.shuffle(imageList);
                adapter = new DpAdapter(getContext(), imageList);
                recyclerView.setAdapter(adapter);

            }
        }

        @Override
        public void onFailure(@NonNull Call<DpDataResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "onFailure: "  + t.getMessage());

        }
    });

    }
}
