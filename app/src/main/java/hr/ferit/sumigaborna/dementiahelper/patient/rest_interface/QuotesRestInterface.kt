package hr.ferit.sumigaborna.dementiahelper.patient.rest_interface

import hr.ferit.sumigaborna.dementiahelper.patient.data.QuotesResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface QuotesRestInterface {
    @GET("/api/quotes")
    fun getQuotes(): Observable<QuotesResponse>
}