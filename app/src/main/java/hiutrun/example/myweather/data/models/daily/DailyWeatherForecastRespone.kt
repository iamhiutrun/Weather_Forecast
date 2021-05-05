package hiutrun.example.myweather.data.models.daily

data class DailyWeatherForecastRespone(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherForecat>,
    val message: Double
)