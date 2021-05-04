package hiutrun.example.myweather.data.api

import hiutrun.example.utils.Constants

class ApiHelper(private val apiService:CurrentWeatherAPI) {
    suspend fun getCurrentWeather(q:String) = apiService.getCurrentWeather(q,Constants.API_KEY)
}