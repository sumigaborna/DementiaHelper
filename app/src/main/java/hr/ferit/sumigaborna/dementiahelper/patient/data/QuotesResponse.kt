package hr.ferit.sumigaborna.dementiahelper.patient.data

class QuotesResponse : ArrayList<QuotesResponseItem>()

data class QuotesResponseItem(
    val author: String,
    val text: String
)