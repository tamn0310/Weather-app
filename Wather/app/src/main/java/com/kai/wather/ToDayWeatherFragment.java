package com.kai.wather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kai.wather.Common.Common;
import com.kai.wather.Model.WeatherResult;
import com.kai.wather.Retrofit.IOpenWeatherMap;
import com.kai.wather.Retrofit.RetrofitClient;
import com.squareup.picasso.Picasso;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDayWeatherFragment extends Fragment {


    ImageView img_weather;
    TextView txt_city_name, txt_humidity, txt_sunrise,
            txt_sunset, txt_wind, txt_pressure, txt_temperature,
            txt_description, txt_date_time,txt_geo_coord;
    LinearLayout weather_panel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static  ToDayWeatherFragment instance;

    public static ToDayWeatherFragment getInstance() {
        if(instance==null){
            instance = new ToDayWeatherFragment();
        }
        return instance;
    }

    public ToDayWeatherFragment() {
        compositeDisposable= new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview = inflater.inflate(R.layout.fragment_to_day_weather, container, false);

        img_weather  = (ImageView)itemview.findViewById(R.id.img_weather);
        txt_city_name  =(TextView) itemview.findViewById(R.id.txt_city_name);
        txt_humidity  =(TextView) itemview.findViewById(R.id.txt_humidity);
        txt_wind  =(TextView) itemview.findViewById(R.id.txt_wind);
        txt_sunrise  =(TextView) itemview.findViewById(R.id.txt_sunrise);
        txt_sunset  =(TextView) itemview.findViewById(R.id.txt_sunset);
        txt_pressure  =(TextView) itemview.findViewById(R.id.txt_pressure);
        txt_temperature  =(TextView) itemview.findViewById(R.id.txt_temperature);
        txt_description  =(TextView) itemview.findViewById(R.id.txt_description);
        txt_date_time  =(TextView) itemview.findViewById(R.id.txt_date_time);
        txt_geo_coord  =(TextView) itemview.findViewById(R.id.txt_geo_coord);

        weather_panel = (LinearLayout)itemview.findViewById(R.id.weather_panel);
        loading = (ProgressBar)itemview.findViewById(R.id.loading);

        getWeatherInfomation();

        return itemview;
    }

    private void getWeatherInfomation() {
       compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
               String.valueOf(Common.current_location.getLongitude()),
               Common.APP_ID,
               "metric")
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<WeatherResult>() {
                   @Override
                   public void accept(WeatherResult weatherResult) throws Exception {

                       //Load image
                       Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                            .append(weatherResult.getWeather().get(0).getIcon())
                        .append(".png").toString()).into(img_weather);

                       //Load Information
                       txt_city_name.setText(weatherResult.getName());
                       txt_description.setText(new StringBuilder("Thời tiết ở ")
                       .append(weatherResult.getName()).toString());

                       txt_temperature.setText(new StringBuilder(
                               String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());

                        txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));

                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());

                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append(" %").toString());

                        txt_sunrise.setText(Common.convertUnixToHour(weatherResult.getSys().getSunrise()));

                        txt_sunset.setText(Common.convertUnixToHour(weatherResult.getSys().getSunset()));

                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());

                        //Display panel
                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                   }


               }, new Consumer<Throwable>() {
                   @Override
                   public void accept(Throwable throwable) throws Exception {
                       Toast.makeText(getActivity(),""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                   }
               })
       );
    }

    //ctrl+o

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
