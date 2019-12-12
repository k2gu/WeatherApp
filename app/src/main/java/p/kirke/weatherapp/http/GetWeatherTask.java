package p.kirke.weatherapp.http;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import p.kirke.weatherapp.data.WeatherResponse;

public class GetWeatherTask extends AsyncTask<String, String, String> {

    //TODO  api key eraldi, küsi asukohta
    private String url = "https://api.openweathermap.org/data/2.5/weather?q=Tallinn&appid=2e2e4852410b620409778022df20b777&units=metric";
    private WeatherResponse weatherResponse;

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder response = new StringBuilder();
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection)
                    url.openConnection();
            BufferedReader
                    reader = new
                    BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            Gson gson = new Gson();
            weatherResponse = gson.fromJson(response.toString(), WeatherResponse.class);
            int i = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
