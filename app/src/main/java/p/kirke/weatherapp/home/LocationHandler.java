package p.kirke.weatherapp.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

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
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        2000,
                        10, getLocationListener(callback));
            }
        }
    }

    private LocationListener getLocationListener(DataCallback callback) {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                callback.onLocationResult(location.getLatitude(), location.getLongitude());
                LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
                if (locationManager != null) {
                    locationManager.removeUpdates(this);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
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
