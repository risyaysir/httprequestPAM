package rpa.mobile.forecastingweather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Page extends AppCompatActivity {

    private TextView tvTemp, tvWindSpeed, tvLatitude, tvLongtitude, tvLocation, tvTodayWeather;
    private ImageView imgWeather;
    private RecyclerView rvWeather;
    private Adapter weatherAdapter;
    private List<Weather> weather = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        this.tvTemp = this.findViewById(R.id.tvTemp);
        this.tvWindSpeed = this.findViewById(R.id.tvWindSpeed);
        this.tvLatitude = this.findViewById(R.id.tvLatitude);
        this.tvLongtitude = this.findViewById(R.id.tvLongtitude);
        this.tvLocation = this.findViewById(R.id.tvLocation);
        this.tvTodayWeather = this.findViewById(R.id.tvWeather);

        this.imgWeather = this.findViewById(R.id.icWeather);
        this.rvWeather = this.findViewById(R.id.rvItem);

        Bundle dataVolley = getIntent().getExtras();
        int weathercode = dataVolley.getInt("weathercode");
        String temperature = dataVolley.getString("temperature");
        String windspeed = dataVolley.getString("windspeed");
        String latitude = dataVolley.getString("latitude");
        String longitude = dataVolley.getString("longitude");
        weather = (List<Weather>) dataVolley.getSerializable("weather");

        Bundle dataRetrofit = getIntent().getExtras();
        int RetrofitCode = dataRetrofit.getInt("code");
        String RetrofitTemp = dataRetrofit.getString("temperature");
        String RetrofitLatitude = dataRetrofit.getString("latitude");
        String RetrofitLongtitude = dataRetrofit.getString("longtitude");
        String RetrofitWindspeed = dataRetrofit.getString("windspeed");
        weather = (List<Weather>) dataRetrofit.getSerializable("weather");

        Weather w = new Weather();
        if (dataVolley != null){
            // Set text
            tvTemp.setText(temperature);
            tvWindSpeed.setText(windspeed);
            tvLatitude.setText(latitude);
            tvLongtitude.setText(longitude);
            if (latitude.equals("-8.0") && longitude.equals("112.625")){
                tvLocation.setText("Malang");
            } else {
                tvLocation.setText("Lokasi");
            }}

        else if (dataRetrofit != null){
            w.setWeathercode(RetrofitCode);
            imgWeather.setImageResource(w.getGambar());
            tvTodayWeather.setText(w.getPrediction());
            tvTemp.setText(RetrofitTemp);
            tvWindSpeed.setText(RetrofitWindspeed);
            tvLatitude.setText(RetrofitLatitude);
            tvLongtitude.setText(RetrofitLongtitude);

        }
        w.setWeathercode(weathercode);
        imgWeather.setImageResource(w.getGambar());
        tvTodayWeather.setText(w.getPrediction());

        weatherAdapter = new Adapter(this, weather);
        rvWeather.setLayoutManager(new LinearLayoutManager(this));
        rvWeather.setAdapter(weatherAdapter);
    }
}
