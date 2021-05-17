package hiutrun.example.myweather.data.models.current

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun toCoord(coord: String): Coord? {
        return Gson().fromJson<Coord>(coord,Coord::class.java)
    }

    @TypeConverter
    fun fromCoord(coord: Coord): String? {
        return Gson().toJson(coord)
    }

    @TypeConverter
    fun toWind(wind: String): Wind? {
        return Gson().fromJson<Wind>(wind,Wind::class.java)
    }

    @TypeConverter
    fun fromWind(wind: Wind): String? {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toMain(main: String): Main? {
        return Gson().fromJson<Main>(main,Main::class.java)
    }

    @TypeConverter
    fun fromMain(main: Main): String? {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toSys(sys: String): Sys? {
        return Gson().fromJson<Sys>(sys,Sys::class.java)
    }

    @TypeConverter
    fun fromSys(sys: Sys): String? {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toWeather(weather: String): List<Weather>? {
        val listType = object : TypeToken<ArrayList<Weather>>(){}.type
        return Gson().fromJson<List<Weather>>(weather,listType)
    }

    @TypeConverter
    fun fromWeather(weather: List<Weather>): String? {
        return Gson().toJson(weather)
    }
}