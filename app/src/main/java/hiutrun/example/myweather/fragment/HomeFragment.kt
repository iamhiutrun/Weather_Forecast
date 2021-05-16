package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.local.DataLocalManager
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.data.models.weather.WeatherForecastRespone
import hiutrun.example.myweather.ui.main.adapter.DailyAdapter
import hiutrun.example.myweather.ui.main.adapter.HourlyAdapter
import hiutrun.example.myweather.ui.main.view.MainActivity
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.myweather.utils.Resource
import hiutrun.example.myweather.utils.Utils.Companion.timeFormat
import hiutrun.example.myweather.utils.Status
import hiutrun.example.myweather.utils.Utils.Companion.getDay
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.tv_degree_min


class HomeFragment : Fragment(R.layout.fragment_home) {
     lateinit var viewModel: WeatherViewModel
     private var dailyAdapter: DailyAdapter = DailyAdapter()
     private var hourlyAdapter: HourlyAdapter = HourlyAdapter()

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        tv_date.text = getDay()

        if(!viewModel.hasInternetConnection()){
            val currentWeatherResponse = DataLocalManager.getCurrentWeather()
            val dailyResponse = DataLocalManager.getDailyWeather()
            retrieveWeather(currentWeatherResponse)
            retrieveDailyWeather(dailyResponse)
        }

        rv_forecast_daily.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        rv_forecast_hourly.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        pullToRefresh.setOnRefreshListener {
            viewModel.getCurrentWeather(viewModel.cityName)
            pullToRefresh.isRefreshing = true
        }

        im_add.setOnClickListener {
            viewModel.getAllCitiesWeather()
            replaceFragment(CityFragment.newInstance(),false)
        }



        viewModel.hourlyWeather.observe(viewLifecycleOwner, Observer{response->
            when(response){
                is Resource.Success ->{
                    response.data?.let { hourlyResponse->
                        retrieveWeather(hourlyResponse)
                    }
                }
                is Resource.Error ->{
                    pullToRefresh.isRefreshing = false
                    Toast.makeText(context,response.message,Toast.LENGTH_LONG).show()
                }
                else -> null
            }
        })

        viewModel.dailyWeather.observe(viewLifecycleOwner, Observer{response->
            when(response){
                is Resource.Success ->{
                    response.data?.let { dailyResponse->
                        retrieveDailyWeather(dailyResponse)
                    }
                }
                else -> null
            }
        })
    }

    private fun retrieveDailyWeather(dailyResponse: WeatherForecastRespone) {
        if(viewModel.hasInternetConnection()){
            DataLocalManager.setDailyWeather(dailyResponse)
        }

        dailyAdapter.setData(dailyResponse.daily)
        rv_forecast_daily.adapter =  dailyAdapter
        hourlyAdapter.setData(dailyResponse.hourly)
        rv_forecast_hourly.adapter = hourlyAdapter
        pullToRefresh.isRefreshing = false
        Toast.makeText(context,"Update successfully",Toast.LENGTH_LONG).show()
    }


    private fun retrieveWeather(weatherResponse: CurrentWeatherResponse) {

        if(viewModel.hasInternetConnection()){
            DataLocalManager.setCurrentWeather(weatherResponse)
        }

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

        tv_city.text = weatherResponse.name
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

    private fun replaceFragment(fragment: Fragment, isTransition : Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if(isTransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        fragmentTransition.add(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }
}