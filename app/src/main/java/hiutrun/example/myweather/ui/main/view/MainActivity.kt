package hiutrun.example.myweather.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.api.ApiHelper
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.myweather.fragment.HomeFragment
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel


class MainActivity : AppCompatActivity() {
    lateinit var viewModel:WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this,
            WeatherModelFactory(application, ApiHelper(RetrofitInstance.api)))
            .get(WeatherViewModel::class.java)
        viewModel.getCurrentWeather("hanoi")
        replaceFragment(HomeFragment.newInstance(),false)
    }

    private fun replaceFragment(fragment: Fragment, isTransition : Boolean) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout,fragment)
        fragmentTransition.commit()
    }
}