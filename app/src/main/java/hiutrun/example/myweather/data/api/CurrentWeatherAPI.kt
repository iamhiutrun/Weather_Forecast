package hiutrun.example.myweather.data.api

import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherAPI {
    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("q")
        q:String,
        @Query("appid")
        appid:String = Constants.API_KEY
    ):Response<CurrentWeatherResponse>
}