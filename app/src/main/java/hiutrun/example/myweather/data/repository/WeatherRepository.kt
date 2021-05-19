package hiutrun.example.myweather.data.repository

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

    fun getAll()= db.getWeatherDao().getAll()

    fun getNameCities()  = db.getWeatherDao().getNameCities()

    suspend fun deleteWeather(id: Int) = db.getWeatherDao().delete(id)

    suspend fun insertWeather(list: List<CurrentWeatherResponse>) = db.getWeatherDao().insert(list)
}