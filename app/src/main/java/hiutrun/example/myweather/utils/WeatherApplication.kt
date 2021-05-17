package hiutrun.example.myweather.utils

import android.app.Application
import hiutrun.example.myweather.data.local.sharedpreferences.DataLocalManager

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataLocalManager.init(applicationContext)
    }
}