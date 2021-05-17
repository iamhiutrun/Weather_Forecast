package hiutrun.example.myweather.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.local.roomdatabase.WeatherDatabase
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import java.lang.IllegalArgumentException

class WeatherModelFactory(private val app: Application,
                          private val weatherRepository: WeatherRepository
                          ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            return WeatherViewModel(app, weatherRepository) as T
        }
        throw IllegalArgumentException("Unkown class name")
    }
}