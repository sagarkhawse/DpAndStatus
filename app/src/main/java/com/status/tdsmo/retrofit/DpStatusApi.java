package com.status.tdsmo.retrofit;


import com.status.tdsmo.models.DpDataResponse;
import com.status.tdsmo.models.Image;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * This app is developed by Sagar Khawse
 * <p>
 * Contact this developer at gmail - sagar.khawse@gmail.com
 * Contact Number :- +917385663427
 * fiverr profile :- {@link "https://www.fiverr.com/s2/c1746e55d6"}
 * <p>
 * Date : - 6 march 2020
 */
public interface DpStatusApi {

    @GET("get_dp_list.php")
    Call<DpDataResponse> getLatestImages();

    @FormUrlEncoded
    @POST("get_dp_by_category.php")
    Call<DpDataResponse> getDpByCategory(@Field("Title") String title);

    @FormUrlEncoded
    @POST("get_category_status.php")
    Call<DpDataResponse> getCategoryStatus(@Field("Language") String language);

    @FormUrlEncoded
    @POST("get_status_list.php")
    Call<DpDataResponse> getStatusList(@Field("Category") String category);

    @FormUrlEncoded
    @POST("get_hindi_status_list.php")
    Call<DpDataResponse> getHindiStatusList(@Field("Category") String category);

    @FormUrlEncoded
    @POST("get_admob_ads.php")
    Call<Image> getAdmobAds(@Field("Id") int id);

    @GET("get_dp_trending.php")
    Call<DpDataResponse> getTrendingList();

    @GET("get_category.php")
    Call<DpDataResponse> getCategoryList();

    @GET("get_more_apps_list.php")
    Call<DpDataResponse> getMoreAppsList();


    @GET("get_all_status.php")
    Call<DpDataResponse> getAllStatus();

    @FormUrlEncoded
    @POST("get_status_paginate.php")
    Call<DpDataResponse> getAllStatus(@Field("page") String page);


}
