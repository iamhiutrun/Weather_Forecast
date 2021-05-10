package hiutrun.example.myweather.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.main.adapter.DailyAdapter
import hiutrun.example.myweather.ui.main.adapter.HourlyAdapter
import hiutrun.example.myweather.ui.main.view.MainActivity
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.myweather.utils.Utils.Companion.timeFormat
import hiutrun.example.myweather.utils.Status
import hiutrun.example.myweather.utils.Utils.Companion.getDay
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.tv_degree_min


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: WeatherViewModel
    private var dailyAdapter: DailyAdapter = DailyAdapter()
    private var hourlyAdapter: HourlyAdapter = HourlyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        updateCurrentWeather()
        tv_date.text = getDay()
        im_add.setOnClickListener {
            findNavController().navigate(R.id.action_generalFragment_to_settingFragment)
        }

        rv_forecast_daily.setHasFixedSize(true)
        rv_forecast_daily.layoutManager = LinearLayoutManager(context)

        rv_forecast_hourly.setHasFixedSize(true)
        rv_forecast_hourly.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        updateDailyWeather()

        pullToRefresh.setOnRefreshListener {
            updateCurrentWeather()
            updateDailyWeather()
            pullToRefresh.isRefreshing = false

        }
    }



    private fun updateCurrentWeather() {
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
        viewModel.getAllData("21.0245","105.8412").observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { weather ->
                            dailyAdapter.setData(weather.daily)
                            rv_forecast_daily.adapter =  dailyAdapter

                            hourlyAdapter.setData(weather.hourly)
                            rv_forecast_hourly.adapter = hourlyAdapter

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
        var status = weatherResponse.weather[0].main

        when(status){
            "Clouds" -> {
               layout_color.setBackgroundResource(R.drawable.gradient_cloudy)
               layout_color_extension.setBackgroundResource(R.drawable.gradient_cloudy_extension)
            }
            "Rain" ->  {
                layout_color.setBackgroundResource(R.drawable.gradient_rainy)
                layout_color_extension.setBackgroundResource(R.drawable.gradient_rainy_extension)
            }
             else ->  {
                 layout_color.setBackgroundResource(R.drawable.gradient_sunny)
                 layout_color_extension.setBackgroundResource(R.drawable.gradient_sunny_extension)
             }
        }

        tv_status.text = weatherResponse.weather[0].main
        tv_degree_max.text = (weatherResponse.main.temp_max.toInt()-273).toString()
        tv_degree_min.text = (weatherResponse.main.temp_min.toInt() - 273).toString()
        tv_feels_like_degree.text = (weatherResponse.main.feels_like.toInt() - 273).toString()
        tv_degree.text = (weatherResponse.main.temp.toInt() - 273).toString()+"°"
        tv_humidity_number.text = weatherResponse.main.humidity.toString() + " %"
        tv_sunrise_time.text = timeFormat.format(weatherResponse.sys.sunrise*1000 -25200000)
        tv_sunset_time.text = timeFormat.format(weatherResponse.sys.sunset*1000-25200000)
        tv_wind_number.text = weatherResponse.wind.speed.toString() + " Km/h"
        tv_pressure_number.text = weatherResponse.main.pressure.toString() +" hPa"
    }


}