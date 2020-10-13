package hr.ferit.sumigaborna.dementiahelper.patient.di

import hr.ferit.sumigaborna.dementiahelper.app.common.QUOTES_RETROFIT
import hr.ferit.sumigaborna.dementiahelper.app.common.WEATHER_RETROFIT
import hr.ferit.sumigaborna.dementiahelper.patient.interactor.QuotesInteractor
import hr.ferit.sumigaborna.dementiahelper.patient.interactor.WeatherInteractor
import hr.ferit.sumigaborna.dementiahelper.patient.rest_interface.QuotesRestInterface
import hr.ferit.sumigaborna.dementiahelper.patient.rest_interface.WeatherRestInterface
import hr.ferit.sumigaborna.dementiahelper.patient.ui.PatientFragmentController
import hr.ferit.sumigaborna.dementiahelper.patient.ui.PatientFragmentListener
import hr.ferit.sumigaborna.dementiahelper.patient.view_model.PatientVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val patientModule = module {
    viewModel { PatientVM(get(),get(),get(),get(),get()) }
    factory { (listener: PatientFragmentListener)->(PatientFragmentController(listener)) }
    factory { WeatherInteractor(get()) }
    factory { provideWeatherRestInterface(get(named(WEATHER_RETROFIT))) }
    factory { QuotesInteractor(get()) }
    factory { provideQuotesRestInterface(get(named(QUOTES_RETROFIT))) }

}

fun provideWeatherRestInterface(retrofit: Retrofit):WeatherRestInterface = retrofit.create(WeatherRestInterface::class.java)
fun provideQuotesRestInterface(retrofit: Retrofit):QuotesRestInterface = retrofit.create(QuotesRestInterface::class.java)