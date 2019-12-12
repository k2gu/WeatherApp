package p.kirke.weatherapp;

import android.content.Context;

import java.util.List;

import p.kirke.weatherapp.db.WeatherDB;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.history.HistoryCallback;

public class WeatherHistoryRepository {

    private WeatherDB db;

    public WeatherHistoryRepository(Context context) {
        db = WeatherDB.getInstance(context);
    }

    public void getAllData(HistoryCallback callback) {
        //TODO backgroundthread
        List<WeatherHistory> historyList = db.weatherHistoryDAO().getAll();
        callback.onResponse(historyList);
    }

    public void insertNewInfo(WeatherHistory history) {
        db.weatherHistoryDAO().insertWeatherHistory(history);
    }
}
