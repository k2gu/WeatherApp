package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;
import p.kirke.weatherapp.http.GetWeatherTask;
import p.kirke.weatherapp.model.WeatherResponse;

public class HomePresenter implements DataCallback {

    private HomeView view;
    private PreferencesSingleton preferencesSingleton;
    private WeatherHistoryRepository repository;

    HomePresenter(HomeView view, PreferencesSingleton preferencesSingleton, WeatherHistoryRepository repository) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
        this.repository = repository;
    }

    void getData() {
        view.showName(preferencesSingleton.getName());
        view.displayUserImage(preferencesSingleton.getPrefPictureLocation());
        view.showLoading(true);
        if (shouldExecuteRequest()) {
            executeRequest();
        }
    }

    private boolean shouldExecuteRequest() {
        return !(hasRequestedDataToday() && isInSameLocation());
    }

    private boolean hasRequestedDataToday() {
        String lastRequestDate = preferencesSingleton.getPrefLastKnownDate();
        // TODO is it today
        return true;
    }

    private boolean isInSameLocation() {
        String lastKnownLocation = preferencesSingleton.getPrefLastKnownLocation();
        // TODO
        return true;
    }

    private void executeRequest() {
        GetWeatherTask task = new GetWeatherTask(this);
        task.execute();
    }

    @Override
    public void onResponse(WeatherResponse response) {
        int roundedActualTemp = roundDoubleToNearestInt(response.getActualTemperature());
        int roundedFeelableTemp = roundDoubleToNearestInt(response.getFeelableTemperature());
        view.showLoading(false);
        view.showWeatherData(response.getCityName(), roundedActualTemp, roundedFeelableTemp);
        //TODO correct date
        repository.insertNewInfo(new WeatherHistory(response.getCityName(), roundedActualTemp, roundedFeelableTemp, "19.12 2019"));
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
