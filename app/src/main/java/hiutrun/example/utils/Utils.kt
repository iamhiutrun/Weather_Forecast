package hiutrun.example.utils

import android.util.Log
import hiutrun.example.myweather.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Utils {
    companion object {
        val timeFormat = SimpleDateFormat("HH:mm")
        val dateFormat = SimpleDateFormat("EEEE")
        val dayFormat = SimpleDateFormat("dd mmm yyyy")

        fun getIconResourceForWeatherCondition(weatherId:Int):Int {
            when(weatherId){
                in 200..232 -> return R.drawable.home;
                in 300..321 -> return R.drawable.ic_rain;
                in 500..504 -> return R.drawable.ic_rain;
                511 -> return R.drawable.ic_snow;
                in 521..531 -> return R.drawable.ic_rain;
                in 600..622 -> return R.drawable.ic_snow;
                in 701..761 -> return R.drawable.ic_fog;
                in 761..781 -> return R.drawable.ic_storm;
                800 -> return R.drawable.ic_clear;
                801 -> return R.drawable.ic_cloud;
                in 802..804 -> return R.drawable.ic_cloud;
            }
            return R.drawable.ic_sun
        }

        fun getDay(): String{
            val sdf = SimpleDateFormat("E, MMM d")
            return sdf.format(Date())
        }

    }
}