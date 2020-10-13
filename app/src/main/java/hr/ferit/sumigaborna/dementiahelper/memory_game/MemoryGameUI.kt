package hr.ferit.sumigaborna.dementiahelper.memory_game

import com.google.firebase.firestore.DocumentSnapshot

data class MemoryGameUI(val allTimeAverageReactionTime:Float,val allTimeAverageScore:Float, val listResult : MutableList<MemoryGameResult>)

data class MemoryGameResult(var id:Int, var score:Int, var memoryTime:MutableList<Float>, var averageMemoryTime:String, var dateAndDay: String)

fun provideMemoryGameUI(documentSnapshot: DocumentSnapshot): MemoryGameUI {
    return if(documentSnapshot.data != null)
        MemoryGameUI(
            getAlltimeMemoryTimeAverage(documentSnapshot.data!!.getValue("listResult")),
            getAlltimeScoreAverage(documentSnapshot.data!!.getValue("listResult")),
            mapListDataToMutableList(documentSnapshot.data!!.getValue("listResult")))

    else MemoryGameUI(0f,0f, mutableListOf())
}

fun getAlltimeMemoryTimeAverage(list: Any): Float {
    var sumTime = 0f
    var listAverage = mutableListOf<Float>()
    (list as List<HashMap<String,Any>>).forEach {
        listAverage.add(it.getValue("averageMemoryTime").toString().substring(0,5).toFloat())
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

private fun mapListDataToMutableList(list: Any): MutableList<MemoryGameResult> {
    val returnList = mutableListOf<MemoryGameResult>()
    (list as List<HashMap<String,Any>>).forEach {
        returnList.add(
            MemoryGameResult(
                id = it.getValue("id").toString().toInt(),
                score = it.getValue("score").toString().toInt(),
                memoryTime = it.getValue("memoryTime") as MutableList<Float>,
                averageMemoryTime = it.getValue("averageMemoryTime").toString(),
                dateAndDay = it.getValue("dateAndDay").toString()
            )
        )
    }
    return returnList
}