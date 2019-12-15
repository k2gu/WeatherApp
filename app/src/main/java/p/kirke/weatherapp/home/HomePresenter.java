package p.kirke.weatherapp.home;

import p.kirke.weatherapp.PermissionHandler;
import p.kirke.weatherapp.PreferencesSingleton;
import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;
import p.kirke.weatherapp.http.GetWeatherTask;
import p.kirke.weatherapp.model.WeatherResponse;
import p.kirke.weatherapp.util.Const;
import p.kirke.weatherapp.util.DateUtil;

public class HomePresenter implements DataCallback {

    private HomeView view;
    private PreferencesSingleton preferencesSingleton;
    private WeatherHistoryRepository repository;
    private PermissionHandler permissionHandler;
    private LocationHandler locationHandler;

    HomePresenter(HomeView view, PreferencesSingleton preferencesSingleton, WeatherHistoryRepository repository,
                  PermissionHandler permissionHandler, LocationHandler locationHandler) {
        this.view = view;
        this.preferencesSingleton = preferencesSingleton;
        this.repository = repository;
        this.permissionHandler = permissionHandler;
        this.locationHandler = locationHandler;
    }

    void start(boolean hasNetworkConnection) {
        view.hideError();
        view.showName(preferencesSingleton.getName());

        if (hasNetworkConnection) {
            view.showLoading(true);
            getLocation();
        } else {
            view.onError(R.string.error_no_internet);
        }
    }

    private void getLocation() {
        if (permissionHandler.hasLocationPermission()) {
            locationHandler.getUserLocation(this);
        } else {
            permissionHandler.requestLocationPermissions();
        }
    }

    @Override
    public void onLocationResult(double latitude, double longitude) {
        if (!(hasRequestedDataToday() && isInSameLocation(latitude, longitude))) {
            executeRequest(latitude, longitude);
        } else {
            repository.getLastEntry(this);
        }
    }

    private boolean hasRequestedDataToday() {
        String lastRequestDate = preferencesSingleton.getPrefLastKnownDate();
        return lastRequestDate.equals(DateUtil.getTodaysDate());
    }

    private boolean isInSameLocation(double latitude, double longitude) {
        String lastKnownLocation = preferencesSingleton.getPrefLastKnownLocation();
        String currentLocation = locationHandler.getSubLocalityFromCoordinates(latitude, longitude);
        return lastKnownLocation.equals(currentLocation);
    }

    private void executeRequest(double latitude, double longitude) {
        GetWeatherTask task = new GetWeatherTask(this);
        task.execute(String.valueOf(latitude), String.valueOf(longitude));
    }

    @Override
    public void onResponse(WeatherResponse response, boolean isCahce) {
        view.showLoading(false);
        if (response == null) {
            view.onError(R.string.error_generic);
        } else {
            int roundedActualTemp = roundDoubleToNearestInt(response.getActualTemperature());
            int roundedFeelableTemp = roundDoubleToNearestInt(response.getFeelableTemperature());
            view.showWeatherData(response.getSubLocalityName(), roundedActualTemp, roundedFeelableTemp);
            if (!isCahce) {
                saveData(response, roundedActualTemp, roundedFeelableTemp);
            }
        }
    }

    private int roundDoubleToNearestInt(double doubleToRound) {
        return (int) Math.round(doubleToRound);
    }

    private void saveData(WeatherResponse response, int roundedActualTemp, int roundedFeelableTemp) {
        String todaysDate = DateUtil.getTodaysDate();
        preferencesSingleton.setPrefLastKnownDate(todaysDate);
        preferencesSingleton.setPrefLastKnownLocation(response.getSubLocalityName());
        repository.addNewHistoryElement(new WeatherHistory(response.getSubLocalityName(), roundedActualTemp, roundedFeelableTemp, todaysDate));
    }

    @Override
    public void onImageSelected(String image) {
        view.displayUserImage(image);
    }

    @Override
    public void onError() {
        view.onError(R.string.error_generic);
    }

    void onPermissionResponse(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Const.LOCATION_REQUEST_CODE) {
            boolean locationPermissionGranted = permissionHandler.isLocationPermissionGranted(requestCode, permissions, grantResults);
            handleLocationResponse(locationPermissionGranted);
        } else if (requestCode == Const.READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            boolean externalStoragePermissionGranted = permissionHandler.isReadExternalStoragePermissionGranted(requestCode, permissions, grantResults);
            handleExternalStorageResponse(externalStoragePermissionGranted);
        }
    }

    private void handleLocationResponse(boolean locationPermissionGranted) {
        if (locationPermissionGranted) {
            locationHandler.getUserLocation(this);
        } else {
            view.onError(R.string.error_denied_location);
            view.showLoading(false);
        }
    }

    private void handleExternalStorageResponse(boolean externalStoragePermissionGranted) {
        if (externalStoragePermissionGranted) {
            view.displayUserImage(preferencesSingleton.getPrefPictureLocation());
        } else {
            view.showImagePlaceholder();
            view.onError(R.string.error_denied_external_storage);
            view.showLoading(false);
        }
    }

    void handleUserImage() {
        if (permissionHandler.hasReadExternalStoragePermission()) {
            view.displayUserImage(preferencesSingleton.getPrefPictureLocation());
        } else {
            permissionHandler.requestReadExternalStoragePermission();
        }
    }
}
