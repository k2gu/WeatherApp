package p.kirke.weatherapp.home;

import p.kirke.weatherapp.model.WeatherResponse;

public interface DataCallback {

    void onResponse(WeatherResponse response, boolean isCache);

    void onImageSelected(String image);

    void onLocationResult(double latitude, double longitude);

    void onError();
}
