package hr.ferit.sumigaborna.dementiahelper.patient.rest_interface

import hr.ferit.sumigaborna.dementiahelper.app.common.WEATHER_API_TOKEN
import hr.ferit.sumigaborna.dementiahelper.patient.data.QuotesResponse
import hr.ferit.sumigaborna.dementiahelper.patient.data.WeatherInfo
import hr.ferit.sumigaborna.dementiahelper.patient.data.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRestInterface {
    @GET("/data/2.5/weather")
    fun getCityWeather(
        @Query("q") city:String,
        @Query("units") units : String = "metric",
        @Query("appid") apiKey : String = WEATHER_API_TOKEN
    ): Observable<WeatherResponse>
}