package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.model.WeatherResponse;
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
        view.showWeatherData(response.getCityName(), roundDoubleToNearestInt(response.getActualTemperature()),
                roundDoubleToNearestInt(response.getFeelableTemperature()));
    }

    private int roundDoubleToNearestInt(double doubleToRound) {
        return  (int) Math.round(doubleToRound);
    }

    @Override
    public void onError() {
        view.showError();
    }
}
