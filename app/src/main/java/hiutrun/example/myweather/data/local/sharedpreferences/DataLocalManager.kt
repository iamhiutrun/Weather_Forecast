package hiutrun.example.myweather.data.local.sharedpreferences

import android.content.Context
import com.google.gson.Gson
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.weather.WeatherForecastResponse
import hiutrun.example.myweather.utils.Constants.Companion.CURRENT_WEATHER_DATA
import hiutrun.example.myweather.utils.Constants.Companion.DAILY_WEATHER_DATA
import hiutrun.example.myweather.utils.Constants.Companion.PREF_FIRST_INSTALL

class DataLocalManager {

    private var weatherSharedPreferences : WeatherSharedPreferences? = null
    companion object{
        private val instance by lazy {
            DataLocalManager()
        }

        fun init(context:Context){
            instance.weatherSharedPreferences = WeatherSharedPreferences(context)
        }

        fun setFirstInstalled(isFirst: Boolean){
            instance.weatherSharedPreferences!!.putBooleanValue(PREF_FIRST_INSTALL,isFirst)
        }

        fun getFirstInstalled() : Boolean{
            return instance.weatherSharedPreferences!!.getBooleanValue(PREF_FIRST_INSTALL)
        }

        fun setCurrentWeather(currentWeatherResponse: CurrentWeatherResponse){
            val data = Gson().toJson(currentWeatherResponse)
            instance.weatherSharedPreferences!!.putStringValue(CURRENT_WEATHER_DATA,data)
        }

        fun getCurrentWeather(): CurrentWeatherResponse{
            val dataString = instance.weatherSharedPreferences!!.getStringValue(CURRENT_WEATHER_DATA)
            return Gson().fromJson(dataString,CurrentWeatherResponse::class.java)
        }

        fun setDailyWeather(weatherForecastResponse: WeatherForecastResponse){
            val data = Gson().toJson(weatherForecastResponse)
            instance.weatherSharedPreferences!!.putStringValue(DAILY_WEATHER_DATA,data)
        }

        fun getDailyWeather(): WeatherForecastResponse{
            val dataString = instance.weatherSharedPreferences!!.getStringValue(DAILY_WEATHER_DATA)
            return Gson().fromJson(dataString,WeatherForecastResponse::class.java)
        }


    }


}


