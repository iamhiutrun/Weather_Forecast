package hiutrun.example.myweather.data.local.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CurrentWeatherResponse>)

    @Query("DELETE FROM current_weather WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM current_weather")
    fun getAll(): LiveData<List<CurrentWeatherResponse>>

    @Query("SELECT name FROM current_weather")
    fun getNameCities() : LiveData<List<String>>

}