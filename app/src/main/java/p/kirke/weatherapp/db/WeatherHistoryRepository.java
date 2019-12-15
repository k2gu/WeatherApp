package p.kirke.weatherapp.db;

import android.content.Context;

import java.util.List;

import p.kirke.weatherapp.ThreadHandler;
import p.kirke.weatherapp.history.HistoryCallback;
import p.kirke.weatherapp.home.DataCallback;
import p.kirke.weatherapp.model.WeatherCharacteristics;
import p.kirke.weatherapp.model.WeatherResponse;

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
            threadHandler.runOnUi(() -> callback.onResponse(historyList));
        });
    }

    public void addNewHistoryElement(WeatherHistory history) {
        threadHandler.runOnBackground(() -> db.weatherHistoryDAO().insertWeatherHistory(history));
    }

    public void getLastEntry(DataCallback callback) {
        threadHandler.runOnBackground(() -> {
            WeatherHistory historyEntry = db.weatherHistoryDAO().getLastElement();
            WeatherResponse response = new WeatherResponse(new WeatherCharacteristics(
                    historyEntry.temperature, historyEntry.feelableTemperature), historyEntry.subLocality);
            threadHandler.runOnUi(() -> callback.onResponse(response, true));
        });
    }
}
