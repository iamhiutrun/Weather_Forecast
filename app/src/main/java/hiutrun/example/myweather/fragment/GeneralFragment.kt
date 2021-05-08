package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.adapter.ForecastAdapter
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.utils.Status
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.android.synthetic.main.fragment_general.tv_degree_min
import java.text.SimpleDateFormat


class GeneralFragment : Fragment(R.layout.fragment_general) {
    private var timeFormat = SimpleDateFormat("HH:mm")
    private lateinit var viewModel: WeatherViewModel
    private var forecastAdapter: ForecastAdapter = ForecastAdapter()

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
        viewModel.getCurrentWeather("hanoi").observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            Log.d("ABC", "updateCurrentWeather: hic ")
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
        viewModel.getAllData("21.0245","105.8412").observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            forecastAdapter.setData(weather.daily)
                            rv_forecast_daily.adapter =  forecastAdapter
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
//        when(weatherResponse.weather[0].main){
//            "Clouds" -> {
//                layout_color.setBackgroundColor(R.drawable.gradient_sunny)
//                Log.d("ABC", "retrieveWeather: Hello")
//            }
//            "Rain" ->  {
//                layout_color.setBackgroundColor(R.drawable.gradient_sunny)
//            }
//             else ->  {
//                 layout_color.setBackgroundColor(R.drawable.gradient_sunny)
//             }
//        }
//        if(weatherResponse.weather[0].main == "Clouds"){
//            layout_color.setBackgroundColor(R.drawable.gradient_sunny)
//        }
//        else if(weatherResponse.weather[0].main == "Rains"){
//            layout_color.setBackgroundColor(R.drawable.gradient_rainy)
//        }
//        else{
//            layout_color.setBackgroundColor(R.drawable.gradient_sunny)
//        }

        tv_status.text = weatherResponse.weather[0].description
        tv_degree_max.text = (weatherResponse.main.temp_max.toInt()-273).toString()
        tv_degree_min.text = (weatherResponse.main.temp_min.toInt() - 273).toString()
        tv_feels_like_degree.text = (weatherResponse.main.feels_like.toInt() - 273).toString()
        tv_degree.text = (weatherResponse.main.temp.toInt() - 273).toString()+"°"
        tv_humidity_number.text = weatherResponse.main.humidity.toString() + " %"
        tv_sunrise_time.text = timeFormat.format(weatherResponse.sys.sunrise*1000)
        tv_sunset_time.text = timeFormat.format(weatherResponse.sys.sunset*1000)
        tv_wind_number.text = weatherResponse.wind.speed.toString() + " Km/h"
        tv_pressure_number.text = weatherResponse.main.pressure.toString() +" hPa"
    }
}