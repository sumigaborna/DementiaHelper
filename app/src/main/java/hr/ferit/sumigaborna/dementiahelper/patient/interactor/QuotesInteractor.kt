package hr.ferit.sumigaborna.dementiahelper.patient.interactor

import hr.ferit.sumigaborna.dementiahelper.patient.data.QuotesResponse
import hr.ferit.sumigaborna.dementiahelper.patient.rest_interface.QuotesRestInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class QuotesInteractor(private val quotesRestInterface : QuotesRestInterface) {
    fun getQuotes() = quotesRestInterface.getQuotes()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturnItem(QuotesResponse())
}