package hr.ferit.sumigaborna.dementiahelper.home.interactor

import hr.ferit.sumigaborna.dementiahelper.home.data.NewsResponse
import hr.ferit.sumigaborna.dementiahelper.home.rest_interface.NewsRestInterface
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsInteractor(private val restInterface: NewsRestInterface){
    fun getHomeNews() =
        restInterface.getScienceDailyNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturnItem(NewsResponse("failed",0, emptyList())
    )
}