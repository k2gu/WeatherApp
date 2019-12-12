package p.kirke.weatherapp.home;

interface HomeView {

    void showName(String name);

    void showWeatherData(String city, String actualTemperature, String feelableTemperature);

    void showError();

    void showLoading(boolean showLoading);
}
