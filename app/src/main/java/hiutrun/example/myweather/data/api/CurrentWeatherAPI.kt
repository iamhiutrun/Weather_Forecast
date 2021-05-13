package hiutrun.example.myweather.data.api

import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.weather.WeatherForecastRespone
import hiutrun.example.myweather.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastAPI {
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q")
        q:String,
        @Query("appid")
        appid:String = Constants.API_KEY
    ): Response<CurrentWeatherResponse>


    @GET("/data/2.5/onecall")
    suspend fun getAllData(
        @Query("lat")
        lat:String,
        @Query("lon")
        lon:String,
        @Query("appid")
        appid:String
    ):Response<WeatherForecastRespone>
}