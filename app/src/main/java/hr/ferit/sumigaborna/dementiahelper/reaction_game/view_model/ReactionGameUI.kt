package hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model

import com.google.firebase.firestore.DocumentSnapshot

data class ReactionGameUI(val allTimeAverageReactionTime:Float, val allTimeAverageScore:Float, val listResult : MutableList<ReactionGameResult>)

data class ReactionGameResult(var id:Int, var score:Int, var reactionTime:MutableList<Float>, var averageReactionTime:String, var dateAndDay: String)

fun provideReactionGameUI(documentSnapshot: DocumentSnapshot):ReactionGameUI{
    return if(documentSnapshot.data != null)
    ReactionGameUI(
        getAlltimeReactionTimeAverage(documentSnapshot.data!!.getValue("listResult")),
        getAlltimeScoreAverage(documentSnapshot.data!!.getValue("listResult")),
        mapListDataToMutableList(documentSnapshot.data!!.getValue("listResult")))

    else ReactionGameUI(0f,0f, mutableListOf())
}

fun getAlltimeReactionTimeAverage(list: Any): Float {
    var sumTime = 0f
    var listAverage = mutableListOf<Float>()
    (list as List<HashMap<String,Any>>).forEach {
        listAverage.add(it.getValue("averageReactionTime").toString().substring(0,5).toFloat())
    }
    listAverage.forEach {
        sumTime+=it
    }
    return sumTime/listAverage.size
}

fun getAlltimeScoreAverage(list: Any): Float {
    var sumTime = 0f
    var listAverage = mutableListOf<Int>()
    (list as List<HashMap<String,Any>>).forEach {
        listAverage.add(it.getValue("score").toString().toInt())
    }
    listAverage.forEach {
        sumTime+=it
    }
    return sumTime/listAverage.size
}

private fun mapListDataToMutableList(list: Any): MutableList<ReactionGameResult> {
    val returnList = mutableListOf<ReactionGameResult>()
    (list as List<HashMap<String,Any>>).forEach {
        returnList.add(
            ReactionGameResult(
                id = it.getValue("id").toString().toInt(),
                score = it.getValue("score").toString().toInt(),
                reactionTime = it.getValue("reactionTime") as MutableList<Float>,
                averageReactionTime = it.getValue("averageReactionTime").toString(),
                dateAndDay = it.getValue("dateAndDay").toString()
            )
        )
    }
    return returnList
}