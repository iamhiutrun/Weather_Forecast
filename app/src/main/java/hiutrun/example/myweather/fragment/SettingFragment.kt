package hiutrun.example.myweather.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.current.Coord
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.main.adapter.CityAdapter
import hiutrun.example.myweather.ui.main.view.MainActivity
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.myweather.utils.Status
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingFragment : Fragment(R.layout.fragment_setting) {

    lateinit var viewModel: WeatherViewModel
    private var listCoord = ArrayList<Coord>()
    private var cityAdapter = CityAdapter()
    private var dataWeather = ArrayList<CurrentWeatherResponse>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        listCoord.add(Coord(21.027764,105.83416))
        listCoord.add(Coord(19.806692,105.785182))
        listCoord.add(Coord(19.234249,104.920037))

        listCoord.forEach { it ->
            pickData(it)
        }

        rv_city.layoutManager = GridLayoutManager(context,2)
        rv_city.setHasFixedSize(true)

        im_back.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private suspend fun x(){

    }
    private fun pickData(coord: Coord){
        viewModel.getCurrentWeather(coord.lat.toString(),coord.lon.toString()).observe(viewLifecycleOwner, Observer {
            it?.let { resources->
                when(resources.status){
                    Status.LOADING->{

                    }

                    Status.SUCCESS-> {
                        resources.data?.let {weather->
                            dataWeather.add(weather)
                            cityAdapter.setData(dataWeather)
                            rv_city.adapter = cityAdapter
                        }
                    }

                    Status.ERROR ->{
                        Toast.makeText(context,resources.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}