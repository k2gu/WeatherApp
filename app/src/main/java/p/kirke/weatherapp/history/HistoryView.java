package p.kirke.weatherapp.history;

import java.util.List;

import p.kirke.weatherapp.ErrorHandlingView;
import p.kirke.weatherapp.db.WeatherHistory;

public interface HistoryView extends ErrorHandlingView {

    void showList(List<WeatherHistory> historyList);

    void showNoInfoYetMessage();

    void hideNoInfoYetMessage();
}
