package hiutrun.example.myweather.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.weather.WeatherForecastRespone
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.myweather.utils.Resource
import hiutrun.example.myweather.utils.WeatherApplication
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class WeatherViewModel(
    val app: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(app){
    val dailyWeather : MutableLiveData<Resource<WeatherForecastRespone>> = MutableLiveData()
    val hourlyWeather : MutableLiveData<Resource<CurrentWeatherResponse>> = MutableLiveData()

    var dailyWeatherResponse : WeatherForecastRespone?=null
    var hourlyWeatherResponse : CurrentWeatherResponse?=null

    init {
        getCurrentWeather("hanoi")
    }

    fun getCurrentWeather(cityName:String) = viewModelScope.launch{
        hourlyWeather.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = weatherRepository.getCurrentWeather(cityName)
                hourlyWeather.postValue(handleCurrentWeather(response))
            }else{
                hourlyWeather.postValue(Resource.Error("No internet"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> hourlyWeather.postValue(Resource.Error("Network Failure"))
                else -> hourlyWeather.postValue(error("Conversion Error"))
            }
        }
    }

    private fun handleCurrentWeather(response: Response<CurrentWeatherResponse>): Resource<CurrentWeatherResponse>? {
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                hourlyWeatherResponse = resultResponse
                val coord = resultResponse.coord
                getDailyWeather(coord.lat.toString(),coord.lon.toString())
                return Resource.Success(hourlyWeatherResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun getDailyWeather(lat:String, lon:String) = viewModelScope.launch{
        dailyWeather.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = weatherRepository.getAllData(lat,lon)
                dailyWeather.postValue(handleDailyWeather(response))
            }else{
                dailyWeather.postValue(Resource.Error("No internet"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> hourlyWeather.postValue(Resource.Error("Network Failure"))
                else -> hourlyWeather.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleDailyWeather(response: Response<WeatherForecastRespone>): Resource<WeatherForecastRespone>? {
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                dailyWeatherResponse = resultResponse
                return Resource.Success(dailyWeatherResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
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