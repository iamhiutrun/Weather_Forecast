package hiutrun.example.myweather.data.local

import android.content.Context
import hiutrun.example.myweather.utils.Constants.Companion.WEATHER_SHARED_PREFERENCES

class WeatherSharedPreferences(val context: Context) {

    fun putBooleanValue(key : String, value : Boolean){
        val sharedPreferences = context.getSharedPreferences(WEATHER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBooleanValue(key : String):Boolean{
        val sharedPreferences = context.getSharedPreferences(WEATHER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key,false)
    }

    fun putStringValue(key: String, value: String){
        val sharedPreferences = context.getSharedPreferences(WEATHER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String):String{
        val sharedPreferences = context.getSharedPreferences(WEATHER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)!!
    }
}