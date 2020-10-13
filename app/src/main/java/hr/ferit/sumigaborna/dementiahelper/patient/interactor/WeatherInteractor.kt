package hr.ferit.sumigaborna.dementiahelper.patient.interactor

import hr.ferit.sumigaborna.dementiahelper.patient.data.Main
import hr.ferit.sumigaborna.dementiahelper.patient.data.WeatherResponse
import hr.ferit.sumigaborna.dementiahelper.patient.rest_interface.WeatherRestInterface
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherInteractor(private val weatherRestInterface : WeatherRestInterface){
    fun getCityWeather(city:String): Observable<WeatherResponse> =
        weatherRestInterface.getCityWeather(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturnItem(WeatherResponse(404,main = Main()))
}
