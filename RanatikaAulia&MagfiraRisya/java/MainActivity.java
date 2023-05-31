package rpa.mobile.forecastingweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    private Button btnVolley, btnRetrofit;
    private RequestQueue requestQueue;
    private List<Weather> weather = new ArrayList<>();
    private API weatherService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnVolley = this.findViewById(R.id.btVolley);
        btnVolley.setOnClickListener(this);

        this.btnRetrofit = this.findViewById(R.id.btRetrofit);
        btnRetrofit.setOnClickListener(this);

        // membuat request queue
        this.requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View view) {
        // membuat request
        if (view == btnVolley) {
            JsonObjectRequest sr = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.open-meteo.com/v1/forecast?latitude=-7.98&longitude=112.63&daily=weathercode&current_weather=true&timezone=auto",
                    null,
                    this,
                    this
            );

            // memasukkan string request ke dalam antrian
            this.requestQueue.add(sr);
        }
        else if (view == btnRetrofit) {
            Gson gson = new GsonBuilder().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.open-meteo.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            weatherService = retrofit.create(API.class);
            double latitude = -7.98;
            double longitude = 112.63;
            Call<Result> call = weatherService.getWeatherForecast(
                    latitude, // latitude
                    longitude, // longitude
                    "weathercode", // daily
                    true, // current_weather
                    "2023-05-19", // start_date
                    "2023-05-25", // end_date
                    "auto" // timezone
            );
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                    if (response.isSuccessful()) {
                        Result weatherResult = response.body();
                        Result.CurrentWeather currentWeather = weatherResult.getCurrentWeather();
                        Result.DailyWeather dailyWeather = weatherResult.getDailyWeather();

                        int code = currentWeather.getWeatherCode();
                        String temperature = currentWeather.getTemperature();
                        String windspeed = currentWeather.getWindSpeed();
                        String latitude = String.valueOf(weatherResult.getLatitude());
                        String longtitude = String.valueOf(weatherResult.getLongitude());

                        String[] time = dailyWeather.getTime();
                        int[] weatherCode = dailyWeather.getWeatherCode();
                        for (int i = 0; i < time.length; i++) {
                            String date = time[i];
                            int weathercode = weatherCode[i];
                            weather.add(new Weather(weathercode, date));
                        }

                        Intent dataRetrofit = new Intent(MainActivity.this, Page.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("code", code);
                        bundle.putString("temperature", temperature);
                        bundle.putString("windspeed", windspeed);
                        bundle.putString("latitude", latitude);
                        bundle.putString("longitude", longtitude);
                        bundle.putSerializable("weather", (Serializable) weather);
                        dataRetrofit.putExtras(bundle);
                        startActivity(dataRetrofit);

                    }

                }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failed to fetch weather data", Toast.LENGTH_SHORT);
                    Log.e("Weather App", "Failed to fetch weather data", t);
                }

            });
        }

    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            // Ambil data dari URL
            // Data current_weather
            String latitude = response.getString("latitude");
            String longitude = response.getString("longitude");
            JSONObject jsonObject = response.getJSONObject("current_weather");
            int weathercode = jsonObject.getInt("weathercode");
            String temperature = jsonObject.getString("temperature");
            String windspeed = jsonObject.getString("windspeed");

            // Data daily
            JSONObject daily = response.getJSONObject("daily");
            JSONArray time = daily.getJSONArray("time");
            JSONArray weathercodes = daily.getJSONArray("weathercode");
            for (int i = 0; i < time.length(); i++){
                String tanggal = time.getString(i);
                int code = weathercodes.getInt(i);
                weather.add(new Weather(code, tanggal));
            }

            // Intent untuk passing ke halaman berikutnya
            Bundle bundle = new Bundle();
            bundle.putInt("weathercode", weathercode);
            bundle.putString("temperature", temperature);
            bundle.putString("windspeed", windspeed);
            bundle.putString("latitude", latitude);
            bundle.putString("longitude", longitude);
            bundle.putSerializable("weather", (Serializable) weather);

            Intent dataVolley = new Intent(MainActivity.this, Page.class);
            dataVolley.putExtras(bundle);
            startActivity(dataVolley);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this, "Gagal mendapat data", Toast.LENGTH_SHORT).show();
    }
}