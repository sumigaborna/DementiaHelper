package hr.ferit.sumigaborna.dementiahelper.home.view_model

import androidx.lifecycle.MutableLiveData
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.home.interactor.NewsInteractor
import io.reactivex.Single

class HomeVM (private val interactor: NewsInteractor):
    BaseViewModel(){

    val homeData = MutableLiveData<HomeUI>()

    fun getNews(): Single<HomeUI> = interactor.getHomeNews().map { provideHomeUI(it) }.doOnSuccess { homeData.value = it }
}