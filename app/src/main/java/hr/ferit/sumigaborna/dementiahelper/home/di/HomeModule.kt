package hr.ferit.sumigaborna.dementiahelper.home.di

import android.view.View
import hr.ferit.sumigaborna.dementiahelper.app.common.NEWS_RETROFIT
import hr.ferit.sumigaborna.dementiahelper.home.interactor.NewsInteractor
import hr.ferit.sumigaborna.dementiahelper.home.rest_interface.NewsRestInterface
import hr.ferit.sumigaborna.dementiahelper.home.ui.HomeItemController
import hr.ferit.sumigaborna.dementiahelper.home.ui.HomeListener
import hr.ferit.sumigaborna.dementiahelper.home.view_model.HomeVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val homeModule = module {
    factory { provideNewsRestInterface(get(named(NEWS_RETROFIT))) }
    factory { NewsInteractor(get()) }
    viewModel { HomeVM(get()) }
    factory { (listener: HomeListener) -> HomeItemController(listener) }
}

fun provideNewsRestInterface(retrofit: Retrofit):NewsRestInterface = retrofit.create(NewsRestInterface::class.java)