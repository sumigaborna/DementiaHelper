package hr.ferit.sumigaborna.dementiahelper.home.data

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles : List<ArticleData>
)

data class ArticleData (
    val articleId:Int,
    val title:String,
    val description: String,
    val url : String,
    val urlToImage : String?,
    val publishedAt : String,
    val content: String
)