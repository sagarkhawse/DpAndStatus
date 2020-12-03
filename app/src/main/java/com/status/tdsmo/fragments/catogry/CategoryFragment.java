package com.status.tdsmo.fragments.catogry;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.status.tdsmo.R;
import com.status.tdsmo.adapters.CategoryListAdapter;
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
public class CategoryFragment extends Fragment {
    private List<Image> categoryList;
    private DpStatusApi mService;
    private RecyclerView recyclerView;
private CategoryListAdapter adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
    recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

    mService = Common.getAPI();
    categoryList = new ArrayList<>();

    loadCategory();
    return view;
    }

    private void loadCategory() {

    mService.getCategoryList().enqueue(new Callback<DpDataResponse>() {
        @Override
        public void onResponse(@NonNull Call<DpDataResponse> call,@NonNull Response<DpDataResponse> response) {
            if (response.isSuccessful()){
                assert response.body() != null;
                if (!response.body().isError()){
                   categoryList =  response.body().getRes();
                   adapter = new CategoryListAdapter(getContext(),categoryList);
                   recyclerView.setAdapter(adapter);

                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<DpDataResponse> call,@NonNull Throwable t) {
            Log.d(TAG, "onFailure: "  + t.getMessage());

        }
    });

    }

}
