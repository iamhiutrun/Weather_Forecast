package hiutrun.example.myweather.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.weather.Hourly
import kotlinx.android.synthetic.main.item_forecast.view.*
import kotlinx.android.synthetic.main.item_forecast_detail.view.*
import kotlinx.android.synthetic.main.item_forecast_detail.view.tv_day
import java.text.SimpleDateFormat

class DetailForecastAdapter(
    private var list: ArrayList<Hourly> = ArrayList<Hourly>()
) : RecyclerView.Adapter<DetailForecastAdapter.ForecastViewHolder>(){
    private val simpleDateFormat = SimpleDateFormat("E")

    inner class ForecastViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    fun setData(list: List<Hourly>){
        this.list = list as ArrayList<Hourly>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_forecast_detail,parent,false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
//        val forcast = list[position]
//        holder.itemView.tv_day.text = simpleDateFormat.format(forcast.dt.toDouble()*1000)
//        holder.itemView.im_weather.setImageResource(R.drawable.ic_arrow_down)
//        holder.itemView.tv_degree.text = (forcast.temp.max.toInt().minus(273)).toString() +" C"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

