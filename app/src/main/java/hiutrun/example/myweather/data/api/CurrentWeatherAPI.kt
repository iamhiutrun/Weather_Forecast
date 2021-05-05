package hiutrun.example.myweather.data.api

import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.daily.DailyWeatherForecastRespone
import hiutrun.example.utils.Constants
import retrofit2.Call
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
    ): CurrentWeatherResponse

    @GET("/data/2.5/forecast/daily")
    suspend fun getDailyWeather(
        @Query("q")
        q:String,
        @Query("cnt")
        cnt:Int,
        @Query("appid")
        appid: String
    ):DailyWeatherForecastRespone
}