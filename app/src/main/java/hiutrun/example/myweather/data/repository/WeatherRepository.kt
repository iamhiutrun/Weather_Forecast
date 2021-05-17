package hiutrun.example.myweather.data.repository

import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.local.roomdatabase.WeatherDatabase
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse

class WeatherRepository(
    private val db: WeatherDatabase
) {
    suspend fun getCurrentWeather(cityName:String)=
        RetrofitInstance.api.getCurrentWeather(cityName)

    suspend fun getAllData(lat:String,lon:String) =
        RetrofitInstance.api.getAllData(lat,lon)

    fun getAll():List<CurrentWeatherResponse> = db.getWeatherDao().getAll()

    suspend fun deleteWeather(weatherResponse: CurrentWeatherResponse) = db.getWeatherDao().delete(weatherResponse)

    suspend fun insertWeather(weatherResponse: CurrentWeatherResponse) = db.getWeatherDao().insert(weatherResponse)
}