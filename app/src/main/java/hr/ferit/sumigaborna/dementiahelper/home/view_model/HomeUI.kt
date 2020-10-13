package hr.ferit.sumigaborna.dementiahelper.home.view_model

import hr.ferit.sumigaborna.dementiahelper.app.common.localizeDate
import hr.ferit.sumigaborna.dementiahelper.home.data.ArticleData
import hr.ferit.sumigaborna.dementiahelper.home.data.NewsResponse

data class HomeUI(var status:String,var newsList : List<ArticleData>)

fun provideHomeUI(response: NewsResponse) : HomeUI {
    val list = mutableListOf<ArticleData>()
    response.articles.forEachIndexed { index, it -> //using only ten news articles
        list.add(ArticleData(index,it.title,it.description,it.url,it.urlToImage,localizeDate(it.publishedAt),it.content ))
    }
    return HomeUI(response.status,list)
}