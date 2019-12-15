package p.kirke.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class WeatherCharacteristics {

    @SerializedName("temperature")
    double temperature;
    @SerializedName("feels_like")
    double feelsLike;

    public WeatherCharacteristics(double temperature, double feelsLike) {
        this.temperature = temperature;
        this.feelsLike = feelsLike;
    }

    public WeatherCharacteristics() {
    }
}
