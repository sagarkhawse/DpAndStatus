package com.status.tdsmo.fragments.statusFragment.pagination.datasource;
/*
 * Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * Paging JetPack
 * */

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.status.tdsmo.common.Common;
import com.status.tdsmo.fragments.statusFragment.viewmodel.StatusNav;
import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;

import java.util.ArrayList;
import java.util.List;

public class StatusDataSource extends PageKeyedDataSource<Long, Image> {
    private long pageKey = 1;
    private int PAGE_COUNT = 1;
    private List<Image> statusList = new ArrayList<>();
    private StatusNav statusNav;

    public StatusDataSource(StatusNav statusNav) {
        this.statusNav = statusNav;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Image> callback) {
    statusNav.setProgress(true);
        AndroidNetworking.post(Common.BASE_URL + Common.STATUS)
                .addBodyParameter("page", String.valueOf(PAGE_COUNT))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(DpDataResponse.class, new ParsedRequestListener<DpDataResponse>() {
                    @Override
                    public void onResponse(DpDataResponse response) {
                        statusNav.setProgress(false);
                        if (response != null && !response.isError()) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                PAGE_COUNT = PAGE_COUNT + 1;
                                statusNav.setListEndStatus(response.getRes().get(response.getRes().size()-1));
                                for (int i = 0; i < response.getRes().size(); i++) {
                                    if (i % 20 == 0) {

                                        statusList.add(new Image("adview").setViewType(6));
                                    } else {

                                        statusList.add(new Image(response.getRes().get(i).getStatus()).setViewType(0));
                                    }
                                }
                                callback.onResult(statusList, null, pageKey);
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        statusNav.setProgress(false);
                        statusNav.setMessage("Server not Responding");
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Image> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Image> callback) {
        statusNav.setProgress(true);
        AndroidNetworking.post(Common.BASE_URL + Common.STATUS)
                .addBodyParameter("page", String.valueOf(PAGE_COUNT))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(DpDataResponse.class, new ParsedRequestListener<DpDataResponse>() {
                    @Override
                    public void onResponse(DpDataResponse response) {
                        statusNav.setProgress(false);
                        if (response != null && !response.isError()) {
                            if (response.getRes() != null && response.getRes().size() > 0) {
                                PAGE_COUNT = PAGE_COUNT + 1;
                                statusNav.setListEndStatus(response.getRes().get(response.getRes().size()-1));
                                for (int i = 0; i < response.getRes().size(); i++) {
                                    if (i % 20 == 0) {

                                      statusList.add(new Image("adview").setViewType(6));
                                    } else {

                                        statusList.add(new Image(response.getRes().get(i).getStatus()).setViewType(0));
                                    }
                                }
                                callback.onResult(statusList, params.key + 1);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        statusNav.setProgress(false);
                        statusNav.setMessage("Server not Responding");
                    }
                });

    }
}
