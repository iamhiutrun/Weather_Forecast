package hiutrun.example.myweather.data.repository

import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.utils.Constants

class WeatherRepository(private val apiHelper:ApiHelper) {
    suspend fun getCurrentWeather(q:String)=
        apiHelper.getCurrentWeather(q)
}