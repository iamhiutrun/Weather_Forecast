package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
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


        rv_forecast_daily.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
//
        rv_forecast_hourly.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

//        pullToRefresh.setOnRefreshListener {
//            updateCurrentWeather("Ca Mau")
//        }

//        im_add.setOnClickListener {
//            replaceFragment(CityFragment.newInstance(),false)
//        }

        viewModel.hourlyWeather.observe(viewLifecycleOwner, Observer{response->
            when(response){
                is Resource.Success ->{
                    response.data?.let { hourlyResponse->
                        retrieveWeather(hourlyResponse)
                    }
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
        dailyAdapter.setData(dailyResponse.daily)
        rv_forecast_daily.adapter =  dailyAdapter
        hourlyAdapter.setData(dailyResponse.hourly)
        rv_forecast_hourly.adapter = hourlyAdapter
    }

//    private fun updateCurrentWeather(cityName:String) {
//        viewModel.getCurrentWeather(cityName).observe(viewLifecycleOwner, Observer {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { weather ->
//                            retrieveWeather(weather)
//                            pullToRefresh.isRefreshing = false
//                        }
//                    }
//                    Status.ERROR -> {
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                    }
//                    Status.LOADING -> {
//
//                    }
//                }
//            }
//        })
//    }

//    private fun updateDailyWeather(lat:String,lon:String){
//        viewModel.getAllData(lat,lon).observe(viewLifecycleOwner, Observer {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { weather ->
//                            pullToRefresh.isRefreshing = false
//                            dailyAdapter.setData(weather.daily)
//                            rv_forecast_daily.adapter =  dailyAdapter
//
//                            hourlyAdapter.setData(weather.hourly)
//                            rv_forecast_hourly.adapter = hourlyAdapter
//
//                        }
//                    }
//                    Status.ERROR -> {
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//                    }
//                    Status.LOADING -> {
//
//                    }
//                }
//            }
//        })
//    }
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
//        val coord = weatherResponse.coord
//        updateDailyWeather(coord.lat.toString(),coord.lon.toString())

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