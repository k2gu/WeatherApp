package p.kirke.weatherapp.db;

import android.content.Context;

import java.util.List;

import p.kirke.weatherapp.history.HistoryCallback;

public class WeatherHistoryRepository {

    private WeatherDB db;

    public WeatherHistoryRepository(Context context) {
        db = WeatherDB.getInstance(context);
    }

    public void getAllData(HistoryCallback callback) {
        //TODO backgroundthread, get all besides todays.
        List<WeatherHistory> historyList = db.weatherHistoryDAO().getAll();
        callback.onResponse(historyList);
    }

    public void addNewHistoryElement(WeatherHistory history) {
        db.weatherHistoryDAO().insertWeatherHistory(history);
    }
}
