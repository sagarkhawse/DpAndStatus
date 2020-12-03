package com.status.tdsmo.fragments.home;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.status.tdsmo.R;
import com.status.tdsmo.adapters.DpAdapter;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<Image> imageList;
    private GridLayoutManager layoutManager;
    private DpStatusApi mService;
    private DpAdapter adapter;
    private ProgressBar progressBar;

    private int page_number;
    private int item_count;

private TextView swipe_down_tv;

    //pagoination vartiable
    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previous_total = 0;
    private int view_threhold = 100;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        imageList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mService = Common.getAPI();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
           Objects.requireNonNull(getActivity()).recreate();
            }
        });

        swipe_down_tv = view.findViewById(R.id.swipe_down_tv);




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mService.getLatestImages().enqueue(new Callback<DpDataResponse>() {
            @Override
            public void onResponse(Call<DpDataResponse> call, Response<DpDataResponse> response) {
                assert response.body() != null;
                if (!response.body().isError()){

                    imageList = response.body().getRes();
                    Collections.shuffle(imageList);
                    adapter = new DpAdapter(getContext(), imageList);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DpDataResponse> call, Throwable t) {

            }
        });

//
//page_number = 1;
//
//        mService.getLatestImages(page_number).enqueue(new Callback<DpDataResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<DpDataResponse> call,@NonNull Response<DpDataResponse> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    imageList = response.body().getRes();
//                    Collections.shuffle(imageList);
//                    if (!response.body().isError()) {
//                        Log.d("CHECK", "onResponse : error FALSE");
//                        adapter = new DpAdapter(getContext(), imageList);
//                        recyclerView.setAdapter(adapter);
//                        progressBar.setVisibility(View.GONE);
//                        if (imageList.size() <= 1){
//                            swipe_down_tv.setVisibility(View.VISIBLE);
//
//
//                        }else {
//                            swipe_down_tv.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<DpDataResponse> call, @NonNull Throwable t) {
//                Log.d(TAG, "onFailure: "  + t.getMessage());
//
//            }
//        });



//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                visibleItemCount = layoutManager.getChildCount();
//                totalItemCount = layoutManager.getItemCount();
//                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//
//
//                if (dy > 0) {
//                    if (isLoading) {
//                        if (totalItemCount > previous_total) {
//                            isLoading = false;
//                            previous_total = totalItemCount;
//                        }
//                    }
//                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + view_threhold)) {
//                        page_number++;
//                        performPagination();
//                        isLoading = true;
//                    }
//                }
//            }
//        });
    }

    private void getImages() {


    }

//    private void performPagination() {
//        progressBar.setVisibility(View.VISIBLE);
//        mService.getLatestImages(page_number).enqueue(new Callback<DpDataResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<DpDataResponse> call,@NonNull Response<DpDataResponse> response) {
//                assert response.body() != null;
//                if (!response.body().isError()) {
//                    List<Image> imageList = response.body().getRes();
//                    Collections.reverse(imageList);
//                    adapter.addImages(imageList);
//
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<DpDataResponse> call,@NonNull Throwable t) {
//                Log.d(TAG, "onFailure: "  + t.getMessage());
//progressBar.setVisibility(View.GONE);
//            }
//        });
//
//
//    }

}
