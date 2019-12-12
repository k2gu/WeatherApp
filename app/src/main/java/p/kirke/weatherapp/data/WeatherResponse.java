package p.kirke.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("main")
    WeatherCaracteristics mainCharacteristics;
    @SerializedName("name")
    String cityName;

    WeatherResponse() {
        // Gson constructor
    }
}
