package p.kirke.weatherapp.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import static android.content.Context.LOCATION_SERVICE;

class LocationManager {

    private Activity activity;

    LocationManager(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    void getUserLocation(DataCallback callback) {
        android.location.LocationManager locationManager = (android.location.LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
        //TODO
        if (locationManager != null && locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                callback.onLocationResult(location.getLatitude(), location.getLongitude());
            }
        }
    }
}
