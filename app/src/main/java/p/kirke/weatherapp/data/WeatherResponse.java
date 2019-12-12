package p.kirke.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("main")
    private WeatherCaracteristics mainCharacteristics;
    @SerializedName("name")
    private String cityName;

    WeatherResponse() {
        // Gson constructor
    }

    public double getActualTemperature() {
        return mainCharacteristics.temp;
    }

    public double getFeelableTemperature() {
        return mainCharacteristics.feels_like;
    }

    public String getCityName() {
        return cityName;
    }
}
