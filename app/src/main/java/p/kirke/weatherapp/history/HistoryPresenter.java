package p.kirke.weatherapp.history;

import java.util.List;

import p.kirke.weatherapp.R;
import p.kirke.weatherapp.db.WeatherHistory;
import p.kirke.weatherapp.db.WeatherHistoryRepository;

public class HistoryPresenter implements HistoryCallback {

    private HistoryView view;
    private WeatherHistoryRepository repository;

    HistoryPresenter(HistoryView view, WeatherHistoryRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void start() {
        repository.getAllData(this);
    }

    @Override
    public void onResponse(List<WeatherHistory> historyList) {
        if (historyList != null) {
            view.showList(historyList);
        } else {
            view.onError(R.string.error_generic);
        }
    }

    @Override
    public void onError() {
        view.onError(R.string.error_generic);
    }
}
