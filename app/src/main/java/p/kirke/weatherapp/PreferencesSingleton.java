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
        return sharedPreferences.getString(PREF_NAME, "");
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(PREF_NAME, name).apply();
    }

    public String getPrefPictureLocation() {
        return sharedPreferences.getString(PREF_PICTURE_LOCATION, "");
    }

    public void setPrefPictureLocation(String location) {
        sharedPreferences.edit().putString(PREF_PICTURE_LOCATION, location).apply();
    }

    public String getPrefLastKnownLocation() {
        return sharedPreferences.getString(PREF_LAST_KNOWN_LOCATION, "");
    }

    public void setPrefLastKnownLocation(String subLocality) {
        sharedPreferences.edit().putString(PREF_LAST_KNOWN_LOCATION, subLocality).apply();
    }

    public String getPrefLastKnownDate() {
        return sharedPreferences.getString(PREF_LAST_KNOWN_DATE, "");
    }

    public void setPrefLastKnownDate(String date) {
        sharedPreferences.edit().putString(PREF_LAST_KNOWN_DATE, date).apply();
    }

    boolean hasUserData() {
        return !sharedPreferences.getString(PREF_NAME, "").isEmpty() &&
                !sharedPreferences.getString(PREF_PICTURE_LOCATION, "").isEmpty();
    }
}
