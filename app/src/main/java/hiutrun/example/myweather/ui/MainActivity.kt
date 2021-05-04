package hiutrun.example.myweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.api.RetrofitInstance
import hiutrun.example.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_see_details.setOnClickListener {
            GlobalScope.launch {
                RetrofitInstance.api.getCurrentWeather("hanoi", Constants.API_KEY)
            }
        }

    }



}