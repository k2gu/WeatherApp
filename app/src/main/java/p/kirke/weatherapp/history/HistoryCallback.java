package p.kirke.weatherapp.history;

import java.util.List;

import p.kirke.weatherapp.db.WeatherHistory;

public interface HistoryCallback {

    void onResponse(List<WeatherHistory> historyList);
}
