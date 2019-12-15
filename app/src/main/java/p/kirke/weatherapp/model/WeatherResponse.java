package p.kirke.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("main")
    private WeatherCharacteristics mainCharacteristics;
    @SerializedName("name")
    private String subLocalityName;

    WeatherResponse() {
        // Gson constructor
    }

    public WeatherResponse(WeatherCharacteristics characteristics, String subLocalityName) {
        this.mainCharacteristics = characteristics;
        this.subLocalityName = subLocalityName;
    }

    public double getActualTemperature() {
        return mainCharacteristics.temperature;
    }

    public double getFeelableTemperature() {
        return mainCharacteristics.feelsLike;
    }

    public String getSubLocalityName() {
        return subLocalityName;
    }
}
