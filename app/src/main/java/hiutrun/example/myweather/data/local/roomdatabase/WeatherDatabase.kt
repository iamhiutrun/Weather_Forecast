package hiutrun.example.myweather.data.local.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hiutrun.example.myweather.data.models.current.Converters
import hiutrun.example.myweather.data.models.current.CurrentWeatherResponse

@Database(
    entities = [CurrentWeatherResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    companion object{
        @Volatile
        private var instance: WeatherDatabase?=null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: createDatabase(context).also{
                instance = it
            }

        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather.db"
            ).build()
    }
}