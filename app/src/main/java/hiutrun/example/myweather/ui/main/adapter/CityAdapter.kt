package hiutrun.example.myweather.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hiutrun.example.myweather.R
import hiutrun.example.myweather.data.models.current.Coord
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse
import hiutrun.example.myweather.utils.Utils.Companion.getIconResourceForWeatherCondition
import kotlinx.android.synthetic.main.item_city_forecast.view.*

class CityAdapter(
    private var list: ArrayList<CurrentWeatherResponse> = ArrayList<CurrentWeatherResponse>()
) : RecyclerView.Adapter<CityAdapter.ForecastViewHolder>(){

    inner class ForecastViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    fun setData(list: ArrayList<CurrentWeatherResponse>){
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context)
                                .inflate(R.layout.item_city_forecast,parent,false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forcast = list[position]
        var i = getIconResourceForWeatherCondition(forcast.weather[0].id)
        holder.itemView.apply {
            im_weather.setImageResource(i)
            tv_degree.text = (forcast.main.temp.toInt().minus(273)).toString()
            tv_city.text = forcast.name
            tv_country.text = forcast.sys.country

            setOnClickListener {
                onItemClickListener?.let {
                    it(forcast.coord)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener:((Coord)->Unit)?=null
    fun setOnItemClickListener(listener: (Coord)->Unit){
        onItemClickListener = listener
    }
}