package hiutrun.example.myweather.data.models.current

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "current_weather")
data class CurrentWeatherResponse(
    val coord: Coord,
    @PrimaryKey
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
): Serializable