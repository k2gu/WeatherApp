package p.kirke.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("main")
    private WeatherCaracteristics mainCharacteristics;
    @SerializedName("name")
    private String subLocalityName;

    WeatherResponse() {
        // Gson constructor
    }

    public double getActualTemperature() {
        return mainCharacteristics.temp;
    }

    public double getFeelableTemperature() {
        return mainCharacteristics.feels_like;
    }

    public String getSubLocalityName() {
        return subLocalityName;
    }
}
