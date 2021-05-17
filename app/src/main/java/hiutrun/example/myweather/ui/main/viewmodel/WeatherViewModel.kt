package hiutrun.example.myweather.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.weather.WeatherForecastResponse
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.myweather.utils.Resource
import hiutrun.example.myweather.utils.WeatherApplication
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException

class WeatherViewModel(
    val app: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(app) {
    private val _dailyWeather: MutableLiveData<Resource<WeatherForecastResponse>> = MutableLiveData()
    private val _hourlyWeather: MutableLiveData<Resource<CurrentWeatherResponse>> =
        MutableLiveData()
    private val _citiesName: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private val _cityWeather: MutableLiveData<ArrayList<Resource<CurrentWeatherResponse>>> = MutableLiveData()

    var cityName = " "

    val dailyWeather: LiveData<Resource<WeatherForecastResponse>>
        get() = _dailyWeather

    val hourlyWeather: LiveData<Resource<CurrentWeatherResponse>>
        get() = _hourlyWeather

    val citiesName: LiveData<ArrayList<String>>
        get() = _citiesName

    val citiesWeather: LiveData<ArrayList<Resource<CurrentWeatherResponse>>>
        get() = _cityWeather

    init {
        _citiesName.postValue(arrayListOf("Nha Trang", "Ninh Binh", "Ca Mau", "Nam Dinh","Ha Dong","Ha Tinh"))
    }


    fun getCurrentWeather(cityName: String) = viewModelScope.launch {
        this@WeatherViewModel.cityName = cityName
        _hourlyWeather.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = weatherRepository.getCurrentWeather(cityName)
                _hourlyWeather.postValue(handleCurrentWeather(response))
            } else {
                _hourlyWeather.postValue(Resource.Error("No internet"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _hourlyWeather.postValue(Resource.Error("Network Failure"))
                else -> _hourlyWeather.postValue(error("Conversion Error"))
            }
        }
    }

    private fun handleCurrentWeather(response: Response<CurrentWeatherResponse>): Resource<CurrentWeatherResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val coord = resultResponse.coord
                getDailyWeather(coord.lat.toString(), coord.lon.toString())
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun getDailyWeather(lat: String, lon: String) = viewModelScope.launch {
        _dailyWeather.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = weatherRepository.getAllData(lat, lon)
                _dailyWeather.postValue(handleDailyWeather(response))
            } else {
                _dailyWeather.postValue(Resource.Error("No internet"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _hourlyWeather.postValue(Resource.Error("Network Failure"))
                else -> _hourlyWeather.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleDailyWeather(response: Response<WeatherForecastResponse>): Resource<WeatherForecastResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<WeatherApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun addCity(cityName: String) {
        var cities = _citiesName.value
        if(!cities!!.contains(cityName))
            cities?.add(cityName)
        _citiesName.postValue(cities)
        getAllCitiesWeather()
    }

    fun getAllCitiesWeather() = viewModelScope.launch {
        var arr = ArrayList<Resource<CurrentWeatherResponse>>()
        supervisorScope {
            for (i in _citiesName.value!!) {
                var value = async { weatherRepository.getCurrentWeather(i) }
                try {
                    handle(value.await())?.let { arr.add(it) }
                }catch (e: Exception){
                    Log.e("ABC", "getAllCitiesWeather: $e")
                }
            }
        }
        _cityWeather.postValue(arr)
    }

    private fun handle(response: Response<CurrentWeatherResponse>): Resource<CurrentWeatherResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}