package p.kirke.weatherapp.home;

import p.kirke.weatherapp.model.WeatherResponse;

public interface DataCallback {

    void onResponse(WeatherResponse response);

    void onError();
}
