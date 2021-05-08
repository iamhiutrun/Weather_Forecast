package hiutrun.example.myweather.data.api

import hiutrun.example.utils.Constants

class ApiHelper(private val apiService:WeatherForecastAPI) {
    suspend fun getCurrentWeather(q:String) =
        apiService.getCurrentWeather(q,Constants.API_KEY)

//    suspend fun getDailyWeatherForecast(q: String) =
//        apiService.getDailyWeather(q,7,Constants.API_KEY)

    suspend fun getAllData(lat:String,lon:String) =
        apiService.getAllData(lat,lon,Constants.API_KEY)

}