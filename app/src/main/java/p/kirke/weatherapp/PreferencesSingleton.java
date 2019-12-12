package p.kirke.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesSingleton {

    private final SharedPreferences sharedPreferences;
    private static PreferencesSingleton preferencesSingleton;

    private static final String SETTINGS = "default_settings";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String PREF_PICTURE_LOCATION = "PREF_PICTURE_LOCATION";
    private static final String PREF_LAST_KNOWN_LOCATION = "PREF_LAST_KNOWN_LOCATION";
    private static final String PREF_LAST_KNOWN_DATE = "PREF_LAST_KNOWN_DATE";

    private PreferencesSingleton(Context context) {
        sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    public static PreferencesSingleton getSingletonInstance(Context context) {
        if (preferencesSingleton == null) {
            preferencesSingleton = new PreferencesSingleton(context);
        }
        return preferencesSingleton;
    }

    public String getName() {
        //TODO
        return sharedPreferences.getString(PREF_NAME, "Kirke");
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(PREF_NAME, name).apply();
    }

    public String getPrefPictureLocation() {
        //TODO
        return sharedPreferences.getString(PREF_PICTURE_LOCATION, "Kirke");
    }

    public void setPrefPictureLocation(String location) {
        sharedPreferences.edit().putString(PREF_PICTURE_LOCATION, location).apply();
    }

    public String getPrefLastKnownLocation() {
        //TODO
        return sharedPreferences.getString(PREF_LAST_KNOWN_LOCATION, "Tallinn");
    }

    public void setPrefLastKnownLocation(String city) {
        sharedPreferences.edit().putString(PREF_LAST_KNOWN_LOCATION, city).apply();
    }

    public String getPrefLastKnownDate() {
        //TODO
        return sharedPreferences.getString(PREF_LAST_KNOWN_DATE, "12.12 2019");
    }

    public void setPrefLastKnownDate(String date) {
        sharedPreferences.edit().putString(PREF_LAST_KNOWN_DATE, date).apply();
    }
}
