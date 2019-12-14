package p.kirke.weatherapp.home;

import p.kirke.weatherapp.ErrorHandlingView;

interface HomeView extends ErrorHandlingView {

    void showName(String name);

    void showWeatherData(String subLocality, int actualTemperature, int feelableTemperature);

    void showLoading(boolean showLoading);

    void displayUserImage(String image);
}
