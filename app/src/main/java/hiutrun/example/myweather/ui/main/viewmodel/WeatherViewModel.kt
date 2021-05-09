package hiutrun.example.myweather.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.utils.Constants
import hiutrun.example.utils.Resource
import hiutrun.example.utils.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    val app: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(app){

    fun getCurrentWeather(q:String) = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            if(hasInternetConnection())
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
            if(hasInternetConnection())
            emit(Resource.success(data = weatherRepository.getAllData(lat,lon)))
        }catch (exception:Exception){
            emit(Resource.error(data = null,message = exception.message?:"Error Occurred"))
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<WeatherApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}