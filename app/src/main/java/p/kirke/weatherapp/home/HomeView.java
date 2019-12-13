package p.kirke.weatherapp.home;

interface HomeView {

    void showName(String name);

    void showWeatherData(String subLocality, int actualTemperature, int feelableTemperature);

    void showError();

    void showLoading(boolean showLoading);

    void displayUserImage(String image);
}
