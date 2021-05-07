package hiutrun.example.myweather.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.ui.main.adapter.DetailForecastAdapter
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(R.layout.fragment_detail) {

    lateinit var viewModel: WeatherViewModel
    private val args: DetailFragmentArgs by navArgs()
    private var detailForecastAdapter = DetailForecastAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_forecast_detail.setHasFixedSize(true)
        rv_forecast_detail.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        updateUI()

    }

    private fun updateUI(){
        val weather = args.weatherForecast
        detailForecastAdapter.setData(weather.list)
        rv_forecast_detail.adapter= detailForecastAdapter
        tv_humidity_degree.text = weather.list[0].humidity.toString() +" %"
        tv_wind_degree.text = weather.list[0].speed.toString() +" km/h"
        tv_pressure_degree.text = weather.list[0].pressure.toString() +" at"
    }

}