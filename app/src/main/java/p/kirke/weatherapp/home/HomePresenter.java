package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.data.WeatherResponse;
import p.kirke.weatherapp.http.GetWeatherTask;

public class HomePresenter implements DataCallback {

    private HomeView view;

    HomePresenter(HomeView view) {
        this.view = view;
    }

    void getData() {
        view.showName(PreferencesSingleton.getName());
        view.showLoading(true);
        executeRequest();
    }

    private void executeRequest() {
        GetWeatherTask task = new GetWeatherTask(this);
        task.execute();
    }

    @Override
    public void onResponse(WeatherResponse response) {
        view.showLoading(false);
        view.showWeatherData(response.getCityName(), roundDoubleAndConvertToString(response.getActualTemperature()),
                roundDoubleAndConvertToString(response.getFeelableTemperature()));
    }

    private String roundDoubleAndConvertToString(double doubleToRound) {
        int doubleAsInt = (int) Math.round(doubleToRound);
        return String.valueOf(doubleAsInt);
    }

    @Override
    public void onError() {
        view.showError();
    }
}
