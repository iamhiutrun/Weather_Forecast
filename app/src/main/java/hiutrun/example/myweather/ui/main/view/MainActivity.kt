package hiutrun.example.myweather.ui.main.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.local.roomdatabase.WeatherDatabase
import hiutrun.example.myweather.data.local.sharedpreferences.DataLocalManager
import hiutrun.example.myweather.data.repository.WeatherRepository
import hiutrun.example.myweather.fragment.HomeFragment
import hiutrun.example.myweather.ui.base.WeatherModelFactory
import hiutrun.example.myweather.ui.main.viewmodel.WeatherViewModel


class MainActivity : AppCompatActivity() {
    lateinit var viewModel:WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!DataLocalManager.getFirstInstalled()){
            Toast.makeText(this, "First Installed",Toast.LENGTH_SHORT).show()
            DataLocalManager.setFirstInstalled(true)
        }
        val repository = WeatherRepository(WeatherDatabase(this))
        viewModel = ViewModelProvider(this,
            WeatherModelFactory(application,repository))
            .get(WeatherViewModel::class.java)
        viewModel.getCurrentWeather("hanoi")
        createNotificationChannel()
        replaceFragment(HomeFragment.newInstance(),false)
    }

    private fun replaceFragment(fragment: Fragment, isTransition : Boolean) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout,fragment)
        fragmentTransition.commit()

    }

    private fun createNotificationChannel(){
        val CHANNEL_ID = getString(R.string.default_notification_channel_id)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name = "FCM"
            val descriptionText = "Hello World"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}