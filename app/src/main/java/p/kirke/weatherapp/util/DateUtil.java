package p.kirke.weatherapp.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    @SuppressLint("SimpleDateFormat")
    public static String getTodaysDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM yyyy");
        Date today = new Date();
        return formatter.format(today);
    }
}
