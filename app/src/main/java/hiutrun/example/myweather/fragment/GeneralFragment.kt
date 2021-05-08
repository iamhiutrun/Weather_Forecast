package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity.apply
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.daily.DailyWeatherForecastRespone
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.adapter.ForecastAdapter
//import hiutrun.example.myweather.ui.main.adapter.ForecastAdapter
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.utils.Status
import kotlinx.android.synthetic.main.fragment_general.*
import okhttp3.internal.wait


class GeneralFragment : Fragment(R.layout.fragment_general) {

    private lateinit var viewModel: WeatherViewModel
    private var forecastAdapter: ForecastAdapter = ForecastAdapter()
    private lateinit var weather:DailyWeatherForecastRespone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateCurrentWeather()
        rv_forecast_daily.setHasFixedSize(true)
        rv_forecast_daily.layoutManager = LinearLayoutManager(context)
        updateDailyWeather()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            WeatherModelFactory(ApiHelper(RetrofitInstance.api))
        ).get(WeatherViewModel::class.java)
    }

    private fun updateCurrentWeather() {
        Log.d("ABC", "updateCurrentWeather: hihi")
        viewModel.getCurrentWeather("hanoi").observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            retrieveWeather(weather)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    private fun updateDailyWeather(){
        viewModel.getDailyWeatherForecast("hanoi").observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            forecastAdapter.setData(weather.list)
                            rv_forecast_daily.adapter =  forecastAdapter
                            //this.weather = weather
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
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
        tv_humidity_number.text = weatherResponse.main.humidity.toString() + " %"
        tv_sunrise_time.text = weatherResponse.sys.sunset.toString()
        tv_sunset_time.text = weatherResponse.sys.sunrise.toString()
        tv_wind_number.text = weatherResponse.wind.speed.toString() + " Km/h"
    }


    private fun retrieveDailyWeather(weatherResponse: DailyWeatherForecastRespone){

    }
}