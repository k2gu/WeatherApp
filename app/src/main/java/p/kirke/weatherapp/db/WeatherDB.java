package p.kirke.weatherapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherHistory.class}, version = 2)
public abstract class WeatherDB extends RoomDatabase {

    private static final String databaseName = "weather_db";
    private static WeatherDB instance;

    public static synchronized WeatherDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), WeatherDB.class, databaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract WeatherHistoryDAO weatherHistoryDAO();
}
