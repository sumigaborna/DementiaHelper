package hr.ferit.sumigaborna.dementiahelper.patient.view_model

import android.content.res.Resources
import hr.ferit.sumigaborna.dementiahelper.app.base.data.BaseTile
import hr.ferit.sumigaborna.dementiahelper.app.common.getTodaysName
import hr.ferit.sumigaborna.dementiahelper.app.common.localizeDate
import hr.ferit.sumigaborna.dementiahelper.patient.data.*
import org.joda.time.DateTime

data class PatientUI(
    var patientCity : String,
    val weatherInfo : WeatherInfo,
    val quotesList : Quotes,
    val patientTiles: List<BaseTile> = emptyList(),
    var loadingDone : Boolean = false
)

fun providePatientUI(patientCity: String,weatherResponse: WeatherResponse, responseQuotes : QuotesResponse, resources: Resources):PatientUI{
    return if(weatherResponse.cod == 200){
        val weatherInfo = WeatherInfo(
            weatherResponse.id,
            getTodaysName(),
            localizeDate(DateTime.now().toString()),
            weatherResponse.name,
            "http://openweathermap.org/img/wn/"+weatherResponse.weather.first().icon+"@2x.png",
            weatherResponse.main.temp.toInt().toString()+"°C",
            weatherResponse.weather.first().description.capitalize()
        )
        PatientUI(patientCity,weatherInfo,
            Quotes(responseQuotes.size,responseQuotes.toList()), provideDefaultPatientTiles(resources)
        )
    }else PatientUI(patientCity,WeatherInfo(), Quotes(), provideDefaultPatientTiles(resources))
}