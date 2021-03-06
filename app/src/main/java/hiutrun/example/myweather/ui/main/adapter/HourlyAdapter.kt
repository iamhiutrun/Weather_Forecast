package hiutrun.example.myweather.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.weather.Hourly
import hiutrun.example.myweather.utils.Utils.Companion.timeFormat
import kotlinx.android.synthetic.main.item_hourly_forecast.view.*

class HourlyAdapter(
    private var list: ArrayList<Hourly> = ArrayList<Hourly>()
) : RecyclerView.Adapter<HourlyAdapter.ForecastViewHolder>(){

    inner class ForecastViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    fun setData(list: List<Hourly>){
        this.list = list as ArrayList<Hourly>
        this.list.removeAt(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_hourly_forecast,parent,false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forcast = list[position]
        holder.itemView.tv_day.text = timeFormat.format(forcast.dt.toDouble()*1000)
        holder.itemView.tv_degree.text = (forcast.temp.toInt().minus(273)).toString() +"°"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

