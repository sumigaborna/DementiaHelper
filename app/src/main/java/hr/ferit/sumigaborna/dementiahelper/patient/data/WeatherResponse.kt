package hr.ferit.sumigaborna.dementiahelper.patient.data

import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING

data class WeatherResponse(
    val cod: Int,
    val id: Int = 0,
    val main: Main,
    val name: String = EMPTY_STRING,
    val weather: List<Weather> = emptyList()
)

data class Main(
    val feels_like: Double = 0.0,
    val humidity: Double= 0.0,
    val pressure: Double= 0.0,
    val temp: Double= 0.0,
    val temp_max: Double= 0.0,
    val temp_min: Double= 0.0
)

data class Weather(
    val description: String = EMPTY_STRING,
    val icon: String= EMPTY_STRING,
    val id: Int = 0,
    val main: String = EMPTY_STRING
)