package p.kirke.weatherapp.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import static android.content.Context.LOCATION_SERVICE;

public class LocationHandler {

    private static final int ACCESS_COARSE_LOCATION = 123;
    //TODO remove
    Activity activity;

    public void requestPermission(Activity activity) {
        this.activity = activity;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_COARSE_LOCATION);
        } else {
            getUserLocation();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_COARSE_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
        Location location = null;
        //TODO
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }
    }
}