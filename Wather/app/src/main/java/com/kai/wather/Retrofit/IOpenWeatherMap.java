package com.kai.wather.Retrofit;

import com.kai.wather.Model.WeatherForecastResult;
import com.kai.wather.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                                 @Query("lon") String  lng,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit );

    @GET("forecast")
    Observable<WeatherForecastResult> getForecastWeatherByLatLng(@Query("lat") String lat,
                                                                 @Query("lon") String  lng,
                                                                 @Query("appid") String appid,
                                                                 @Query("units") String unit );

    @GET("weather")
    Observable<WeatherResult> getWeatherByCityName(@Query("q") String cityname,
                                                 @Query("appid") String appid,
                                                 @Query("units") String unit );



}
