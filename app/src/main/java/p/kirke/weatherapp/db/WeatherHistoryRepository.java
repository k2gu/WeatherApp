package p.kirke.weatherapp.db;

import android.content.Context;

import java.util.Collections;
import java.util.List;

import p.kirke.weatherapp.ThreadHandler;
import p.kirke.weatherapp.history.HistoryCallback;

public class WeatherHistoryRepository {

    private WeatherDB db;
    private ThreadHandler threadHandler;

    public WeatherHistoryRepository(Context context) {
        db = WeatherDB.getInstance(context);
        threadHandler = ThreadHandler.getInstance();
    }

    public void getAllData(HistoryCallback callback) {
        threadHandler.runOnBackground(() -> {
            List<WeatherHistory> historyList = db.weatherHistoryDAO().getAll();
            Collections.reverse(historyList);
            historyList.remove(0);
            threadHandler.runOnUi(() -> callback.onResponse(historyList));
        });
    }

    public void addNewHistoryElement(WeatherHistory history) {
        threadHandler.runOnBackground(() -> db.weatherHistoryDAO().insertWeatherHistory(history));
    }
}
