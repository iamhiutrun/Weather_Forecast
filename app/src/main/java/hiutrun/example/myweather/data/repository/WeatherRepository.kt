package hiutrun.example.myweather.data.repository

import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.utils.Constants

class WeatherRepository {
    suspend fun getCurrentWeather(q:String)=
        RetrofitInstance.api.getCurrentWeather("hanoi",Constants.API_KEY)
}