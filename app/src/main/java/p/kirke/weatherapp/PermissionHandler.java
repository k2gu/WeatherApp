package p.kirke.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import p.kirke.weatherapp.util.Const;

public class PermissionHandler {

    private Activity activity;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
    }

    public boolean hasLocationPermission() {
        //TODO fine? coarse?
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public boolean hasReadExternalStoragePermission() {
        return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private boolean hasPermission(String permissions) {
        return ContextCompat.checkSelfPermission(activity, permissions) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermissions() {
        requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Const.LOCATION_REQUEST_CODE);
    }

    public void requestReadExternalStoragePermission() {
        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Const.READ_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
