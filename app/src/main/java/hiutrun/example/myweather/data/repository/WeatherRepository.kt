package hiutrun.example.myweather.data.repository

import hiutrun.example.myweather.data.api.ApiHelper

class WeatherRepository(private val apiHelper:ApiHelper) {
    suspend fun getCurrentWeather(cityName:String)=
        apiHelper.getCurrentWeather(cityName)

//    suspend fun getDailyWeather(q:String)=
//        apiHelper.getDailyWeatherForecast(q)

    suspend fun getAllData(lat:String,lon:String) =
        apiHelper.getAllData(lat,lon)
}