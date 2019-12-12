package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.http.GetWeatherTask;
import p.kirke.weatherapp.model.WeatherResponse;

public class HomePresenter implements DataCallback {

    private HomeView view;
    private PreferencesSingleton preferencesSingleton;

    HomePresenter(HomeView view, PreferencesSingleton preferencesSingleton) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
    }

    void getData() {
        view.openGalleryOnClickImage();
        view.showName(preferencesSingleton.getName());
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

    @Override
    public void onImageSelected(String image) {
        view.displayUserImage(image);
    }

    private int roundDoubleToNearestInt(double doubleToRound) {
        return (int) Math.round(doubleToRound);
    }

    @Override
    public void onError() {
        view.showError();
    }
}
