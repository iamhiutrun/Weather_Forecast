package hiutrun.example.myweather.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.weather.Daily
import hiutrun.example.myweather.utils.Utils.Companion.dateFormat
import hiutrun.example.myweather.utils.Utils.Companion.getIconResourceForWeatherCondition
import kotlinx.android.synthetic.main.item_daily_forecast.view.*

class DailyAdapter(
    private var list: ArrayList<Daily> = ArrayList<Daily>()
) : RecyclerView.Adapter<DailyAdapter.ForecastViewHolder>(){

    inner class ForecastViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    fun setData(list: List<Daily>){
        this.list = list as ArrayList<Daily>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_daily_forecast,parent,false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forcast = list[position]
        var i = getIconResourceForWeatherCondition(forcast.weather[0].id)
        holder.itemView.im_weather.setImageResource(i)
        holder.itemView.tv_day.text = dateFormat.format(forcast.dt.toDouble()*1000)
        holder.itemView.tv_degree_max.text = (forcast.temp.max.toInt().minus(273)).toString()
        holder.itemView.tv_degree_min.text = (forcast.temp.min.toInt().minus(273)).toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}