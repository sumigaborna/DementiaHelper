package hr.ferit.sumigaborna.dementiahelper.home.rest_interface

import hr.ferit.sumigaborna.dementiahelper.app.common.NEWS_API_TOKEN
import hr.ferit.sumigaborna.dementiahelper.home.data.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsRestInterface{
    @GET("v2/everything")
    fun getScienceDailyNews(
        @Query("q") categoryName:String="Dementia",
        @Query("domains") domainName: String = "sciencedaily.com",
        @Query("apiKey") apiKey : String = NEWS_API_TOKEN
    ): Single<NewsResponse>
}