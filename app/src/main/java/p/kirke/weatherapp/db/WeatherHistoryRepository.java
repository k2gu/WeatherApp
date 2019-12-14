package p.kirke.weatherapp.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;

import p.kirke.weatherapp.history.HistoryCallback;

public class WeatherHistoryRepository {

    private WeatherDB db;
    private HistoryCallback callback;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            if (inputMessage.what == 210) {
                try {
                    callback.onResponse((List<WeatherHistory>) inputMessage.obj);
                } catch (ClassCastException exception) {
                    callback.onError();
                    // TODO error
                }
            }
        }
    };

    public WeatherHistoryRepository(Context context) {
        db = WeatherDB.getInstance(context);
    }

    public void getAllData(HistoryCallback callback) {
        this.callback = callback;
        new Thread(() -> {
            List<WeatherHistory> historyList = db.weatherHistoryDAO().getAll();

            Message message = handler.obtainMessage(210, historyList);
            handler.sendMessage(message);
        }).start();
    }

    public void addNewHistoryElement(WeatherHistory history) {
        new Thread(() -> db.weatherHistoryDAO().insertWeatherHistory(history)).start();
    }
}
