package p.kirke.weatherapp.home;

import p.kirke.weatherapp.data.WeatherResponse;

public interface DataCallback {

    void onResponse(WeatherResponse response);

    void onError();
}
