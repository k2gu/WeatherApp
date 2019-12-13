package p.kirke.weatherapp.http;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import p.kirke.weatherapp.home.DataCallback;
import p.kirke.weatherapp.model.WeatherResponse;
import p.kirke.weatherapp.util.Const;

public class GetWeatherTask extends AsyncTask<String, String, String> {

    private DataCallback callback;

    public GetWeatherTask(DataCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder response = new StringBuilder();
            URL url = new URL(getUrl(strings[0], strings[1]));
            HttpURLConnection urlConnection = (HttpURLConnection)
                    url.openConnection();
            BufferedReader
                    reader = new
                    BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUrl(String latitude, String longitude) {
        // TODO api key
        return String.format(Const.GET_WEATHER_INFO_URL, latitude, longitude, "2e2e4852410b620409778022df20b777");
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);
        callback.onResponse(weatherResponse);
    }
}
