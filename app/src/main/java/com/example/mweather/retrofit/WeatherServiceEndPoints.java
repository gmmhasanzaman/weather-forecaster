package com.example.mweather.retrofit;



import com.example.mweather.model.WeatherApi.WeatherResponse;
import com.example.mweather.model.WeatherForecaster.WeatherForecaster;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherServiceEndPoints {

    @GET("data/2.5/weather?")
    Call<WeatherResponse> getByLetLng(
            @Query("lat") String lat,
            @Query("lon") String lng,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("/data/2.5/forecast?")
    Call<WeatherForecaster> getWeatherForecaster(
            @Query("lat") String lat,
            @Query("lon") String lng,
            @Query("units") String units,
            @Query("appid") String apiKey
    );
}
