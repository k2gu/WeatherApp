package p.kirke.weatherapp;

public class PreferencesSingleton {

    public static String getName() {
        return "Kirke";
    }

    public static int[] getLastKnownLocation() {
        return new int[]{100, 50};
    }

    public static long getLastKnownDate() {
        return 0;
    }
}
