package com.cachewithrealm.api;

import com.cachewithrealm.api.model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Bekti on 05/07/2017.
 */

public interface WeatherService {

    @GET("weather?units=metric")
    Observable<WeatherResponse> getWeather(@Query("q") String city, @Query("appid") String apiKey);

}
