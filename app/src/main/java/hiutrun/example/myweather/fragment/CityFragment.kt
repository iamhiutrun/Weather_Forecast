package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.main.adapter.CityAdapter
import hiutrun.example.myweather.ui.main.view.MainActivity
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.myweather.utils.Resource
import kotlinx.android.synthetic.main.fragment_setting.*

class CityFragment : Fragment(R.layout.fragment_setting) {

    lateinit var viewModel: WeatherViewModel
    private var listCoord = ArrayList<String>()
    private var cityAdapter = CityAdapter()
    private var dataWeather = ArrayList<CurrentWeatherResponse>()

    companion object {
        @JvmStatic
        fun newInstance() =
            CityFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        rv_city.setHasFixedSize(true)
        rv_city.layoutManager = GridLayoutManager(context,2)


        cityAdapter.setOnItemClickListener {
            viewModel.getCurrentWeather(it)
            removeFragment()
        }

        viewModel.citiesWeather.observe(viewLifecycleOwner, Observer { response ->
            var cities = ArrayList<CurrentWeatherResponse>()
            for (i in response) {
                when (i) {
                    is Resource.Success -> {
                        i.data?.let { cities.add(it) }
                    }
                    else -> null
                }
            }
            cityAdapter.setData(cities)
            rv_city.adapter = cityAdapter
        })
        search_view.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.addCity(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
    }



    private fun removeFragment() = activity!!.supportFragmentManager.popBackStack()
}