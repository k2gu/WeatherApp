package p.kirke.weatherapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "temperature")
    public int temperature;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "feelable_temperature")
    public int feelableTemperature;

    public WeatherHistory(String city, int temperature, int feelableTemperature, String date) {
        this.city = city;
        this.temperature = temperature;
        this.feelableTemperature = feelableTemperature;
        this.date = date;
    }
}
