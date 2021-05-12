package hiutrun.example.myweather.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.current.Coord
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.ui.main.adapter.CityAdapter
import hiutrun.example.myweather.ui.main.view.MainActivity
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel
import hiutrun.example.myweather.utils.Status
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
        listCoord.add("Nha Trang")
        listCoord.add("Ninh Binh")
        listCoord.add("Ca Mau")
        listCoord.add("Nam Dinh")

        listCoord.forEach { it ->
            pickData(it)
        }

        rv_city.layoutManager = GridLayoutManager(context,2)
        rv_city.setHasFixedSize(true)
        cityAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("coor",it)
            }
            removeFragment(this,false)
        }

    }

    private fun pickData(cityName: String){
        viewModel.getCurrentWeather(cityName).observe(viewLifecycleOwner, Observer {
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

    private fun removeFragment(fragment: Fragment, isTransition : Boolean) {
        val fragmentTransition = activity!!.supportFragmentManager.beginTransaction()

        if(isTransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        fragmentTransition.remove(fragment)
        fragmentTransition.commit()
    }
}