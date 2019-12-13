package p.kirke.weatherapp.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

class LocationHandler {

    private Activity activity;

    LocationHandler(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    void getUserLocation(DataCallback callback) {
        LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
        //TODO
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                callback.onLocationResult(location.getLatitude(), location.getLongitude());
            }
        }
    }

    String getSubLocalityFromCoordinates(double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getSubLocality();
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }
}
