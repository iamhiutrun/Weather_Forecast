package hiutrun.example.myweather.data.local.roomdatabase

import androidx.room.*
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherResponse: CurrentWeatherResponse)

    @Delete
    suspend fun delete(weatherResponse: CurrentWeatherResponse)

    @Query("SELECT * FROM current_weather")
    fun getAll(): List<CurrentWeatherResponse>

}