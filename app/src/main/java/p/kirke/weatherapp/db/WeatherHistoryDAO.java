package p.kirke.weatherapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherHistoryDAO {
    @Query("SELECT * FROM weatherhistory")
    List<WeatherHistory> getAll();

    @Insert
    void insertWeatherHistory(WeatherHistory history);


}
