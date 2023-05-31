package rpa.mobile.forecastingweather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    @GET("forecast")
    Call<Result> getWeatherForecast(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("daily") String daily,
            @Query("current_weather") boolean currentWeather,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate,
            @Query("timezone") String timezone
    );
}
