package hiutrun.example.myweather.data.models.weather

data class WeatherForecastResponse(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
)