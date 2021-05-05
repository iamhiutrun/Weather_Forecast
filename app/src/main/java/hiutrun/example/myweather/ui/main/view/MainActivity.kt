package hiutrun.example.myweather.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.adapter.ForecastAdapter
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.utils.Constants
import hiutrun.example.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel
    private var forecastAdapter:ForecastAdapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        updateCurrentWeather()
        rv_forecast.setHasFixedSize(true)
        rv_forecast.layoutManager = LinearLayoutManager(this)
        btn_see_details.setOnClickListener {
            updateDailyWeather()
        }
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            WeatherModelFactory(ApiHelper(RetrofitInstance.api))
        ).get(WeatherViewModel::class.java)
    }

    private fun updateCurrentWeather() {
        viewModel.getCurrentWeather("hanoi").observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            retrieveWeather(weather)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun updateDailyWeather(){
        viewModel.getDailyWeatherForecast("hanoi").observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            Log.d("ABC", "updateDailyWeather: DONE")
                            forecastAdapter.setData(weather.list)
                            rv_forecast.adapter =  forecastAdapter
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }
    private fun retrieveWeather(weatherResponse: CurrentWeatherResponse) {
        tv_status.text = weatherResponse.weather[0].description
        tv_degree.text = (weatherResponse.main.temp.toInt() - 273).toString()
        tv_humidity.text = weatherResponse.main.humidity.toString() + " %"
        tv_wind.text = weatherResponse.wind.speed.toString() + " Km/h"
        tv_other.text = weatherResponse.visibility.toString()
    }


}