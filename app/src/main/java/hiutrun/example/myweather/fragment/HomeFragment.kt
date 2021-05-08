package hiutrun.example.myweather.fragment

import android.os.Bundle
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
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.adapter.DailyAdapter
import hiutrun.example.myweather.ui.main.adapter.HourlyAdapter
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.utils.Status
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.tv_degree_min
import java.text.SimpleDateFormat


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var timeFormat = SimpleDateFormat("HH:mm")
    private lateinit var viewModel: WeatherViewModel
    private var dailyAdapter: DailyAdapter = DailyAdapter()
    private var hourlyAdapter: HourlyAdapter = HourlyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateCurrentWeather()
        im_add.setOnClickListener {
            findNavController().navigate(R.id.action_generalFragment_to_settingFragment)
        }
        rv_forecast_daily.setHasFixedSize(true)
        rv_forecast_daily.layoutManager = LinearLayoutManager(context)

        rv_forecast_hourly.setHasFixedSize(true)
        rv_forecast_hourly.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
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