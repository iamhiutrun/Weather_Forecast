package hiutrun.example.myweather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.utils.Constants
import kotlinx.coroutines.launch

class WeatherViewModel(
    val app:Application,
    val weatherRepository: WeatherRepository
) : AndroidViewModel(app){


}