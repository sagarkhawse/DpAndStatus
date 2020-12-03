package com.status.tdsmo.common;


import com.status.tdsmo.models.Image;
import com.status.tdsmo.retrofit.DpStatusApi;
import com.status.tdsmo.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Common {
    public static boolean isLoginSelected = false;
    public static List<Image> imageList ;
    public static List<Image> statusList;
    public static Image statusItem;
    public static int position ;
    public static String banner = "ca-app-pub-1574991073654598/3372604036";
    public static String interstitial = "ca-app-pub-1574991073654598/2989460654";

    public static  boolean isShare = false;
    public static  boolean isShareWhatsapp = false;
    public static  boolean isShareFb = false;
    public static  boolean isShareInstagram = false;
    public static final String BASE_URL = "https://dplove.in/DpStatus/";
    public static final String STATUS = "get_status_paginate.php";
//private static final String BASE_URL = "https://skdeveloperteam101.000webhostapp.com/DpStatus/";

    public static DpStatusApi getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(DpStatusApi.class);
    }
}
