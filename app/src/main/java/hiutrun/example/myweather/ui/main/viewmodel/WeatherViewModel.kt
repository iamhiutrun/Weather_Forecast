package hiutrun.example.myweather.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.utils.Constants
import hiutrun.example.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    val weatherRepository: WeatherRepository
) : ViewModel(){

    fun getCurrentWeather(q:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherRepository.getCurrentWeather(q)))
        }catch (exception:Exception){
            emit(Resource.error(data = null,message = exception.message?:"Error Occurred!"))
        }
    }

//    fun getDailyWeatherForecast(q:String) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(Resource.success(data = weatherRepository.getDailyWeather(q)))
//        }catch (exception:Exception){
//            emit(Resource.error(data = null,message = exception.message?:"Error Occurred"))
//        }
//    }

    fun getAllData(lat:String,lon:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = weatherRepository.getAllData(lat,lon)))
        }catch (exception:Exception){
            emit(Resource.error(data = null,message = exception.message?:"Error Occurred"))
        }
    }

}