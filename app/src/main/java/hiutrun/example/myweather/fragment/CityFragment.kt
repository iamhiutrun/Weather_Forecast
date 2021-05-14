package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
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

        rv_city.layoutManager = GridLayoutManager(context,2)
        rv_city.setHasFixedSize(true)

        cityAdapter.setOnItemClickListener {
            removeFragment(this,false)
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
    }



    private fun removeFragment(fragment: Fragment, isTransition : Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if(isTransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        fragmentTransition.remove(fragment)
        fragmentTransition.commit()
    }
}