package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class DetailFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var forecastAdapter: ForecastAdapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        updateCurrentWeather()
        rv_forecast.setHasFixedSize(true)
        rv_forecast.layoutManager = LinearLayoutManager(context)
        btn_see_details.setOnClickListener {
            updateDailyWeather()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
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
        viewModel.getDailyWeatherForecast("hanoi").observe(viewLifecycleOwner, Observer {
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
        tv_humidity.text = weatherResponse.main.humidity.toString() + " %"
        tv_wind.text = weatherResponse.wind.speed.toString() + " Km/h"
        tv_other.text = weatherResponse.visibility.toString()
    }

}