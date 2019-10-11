package com.zzq.app.http.service;

import com.zzq.app.bean.OneNETBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface OneNETService {
    String API_ONENET = "https://api.heclouds.com/devices/";

    @GET("503698659/datapoints")
    Observable<OneNETBean> fetchRealtimeData();
}
