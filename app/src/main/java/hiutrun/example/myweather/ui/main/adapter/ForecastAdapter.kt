package hiutrun.example.myweather.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.daily.WeatherForcast
import kotlinx.android.synthetic.main.activity_main.view.*

class ForecastAdapter(
    private var list: ArrayList<WeatherForcast> = ArrayList<WeatherForcast>()
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>(){

    inner class ForecastViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    fun setData(list: List<WeatherForcast>){
        this.list = list as ArrayList<WeatherForcast>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_forecast,parent,false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forcast = list?.get(position)
        holder.itemView.tv_degree.text = (forcast?.temp?.max?.toInt().minus(273)).toString() +" C"
    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }
}