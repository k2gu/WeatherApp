package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;
import p.kirke.weatherapp.http.GetWeatherTask;
import p.kirke.weatherapp.model.WeatherResponse;
import p.kirke.weatherapp.util.Const;

public class HomePresenter implements DataCallback {

    private HomeView view;
    private PreferencesSingleton preferencesSingleton;
    private WeatherHistoryRepository repository;
    private PermissionHandler permissionHandler;
    private LocationManager locationManager;

    HomePresenter(HomeView view, PreferencesSingleton preferencesSingleton, WeatherHistoryRepository repository,
                  PermissionHandler permissionHandler, LocationManager locationManager) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
        this.repository = repository;
        this.permissionHandler = permissionHandler;
        this.locationManager = locationManager;
    }

    void start() {
        view.showName(preferencesSingleton.getName());
        view.displayUserImage(preferencesSingleton.getPrefPictureLocation());
        if (permissionHandler.hasLocationPermission()) {
            locationManager.getUserLocation(this);
        } else {
            permissionHandler.requestLocationPermissions();
        }
    }

    @Override
    public void onLocationResult(double latitude, double longitude) {
        if (shouldExecuteRequest()) {
            view.showLoading(true);
            executeRequest(latitude, longitude);
        }
    }

    private boolean shouldExecuteRequest() {
        return !(hasRequestedDataToday() && isInSameLocation());
    }

    private void executeRequest(double latitude, double longitude) {
        GetWeatherTask task = new GetWeatherTask(this);
        task.execute(String.valueOf(latitude), String.valueOf(longitude));
    }

    private boolean hasRequestedDataToday() {
        String lastRequestDate = preferencesSingleton.getPrefLastKnownDate();
        // TODO is it today
        return false;
    }

    private boolean isInSameLocation() {
        String lastKnownLocation = preferencesSingleton.getPrefLastKnownLocation();
        // TODO
        return true;
    }

    @Override
    public void onResponse(WeatherResponse response) {
        int roundedActualTemp = roundDoubleToNearestInt(response.getActualTemperature());
        int roundedFeelableTemp = roundDoubleToNearestInt(response.getFeelableTemperature());
        saveLocationToPrefSingleton(response.getCityName());
        view.showLoading(false);
        view.showWeatherData(response.getCityName(), roundedActualTemp, roundedFeelableTemp);
        //TODO correct date
        repository.insertNewInfo(new WeatherHistory(response.getCityName(), roundedActualTemp, roundedFeelableTemp, "19.12 2019"));
    }

    private void saveLocationToPrefSingleton(String cityName) {
        preferencesSingleton.setPrefPictureLocation(cityName);
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

    void onPermissionResponse(int requestCode, String[] permissions, int[] grantResults) {
        // TODO what if cancelled?
        if (requestCode == Const.LOCATION_REQUEST_CODE) {
            locationManager.getUserLocation(this);
        }
    }
}
