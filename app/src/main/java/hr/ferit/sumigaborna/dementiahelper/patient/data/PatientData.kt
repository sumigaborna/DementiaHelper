package hr.ferit.sumigaborna.dementiahelper.patient.data

import android.content.res.Resources
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.data.BaseTile
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.localizeDate
import org.joda.time.DateTime
import java.text.DateFormatSymbols
import java.time.LocalDateTime
import java.util.*

data class WeatherInfo(
    val id: Int=0,
    val day: String= EMPTY_STRING,
    val date: String= EMPTY_STRING,
    val city: String= EMPTY_STRING,
    val iconURL : String= EMPTY_STRING,
    val temp : String= EMPTY_STRING,
    val weatherDescription : String= EMPTY_STRING
)

data class Quotes(
    val id: Int=0,
    val quotes: List<QuotesResponseItem> = emptyList()
)

fun provideDefaultPatientTiles(resources: Resources): List<BaseTile> {
    val tileMemoryLane = BaseTile(0, R.drawable.ic_memory_lane,resources.getString(R.string.memory_lane))
    val tileTODO = BaseTile(1, R.drawable.ic_todo,resources.getString(R.string.todos))
    val tileNotes = BaseTile(2, R.drawable.ic_notes,resources.getString(R.string.notes))
    val tileEmergency = BaseTile(3, R.drawable.ic_emergency_call,resources.getString(R.string.emergency_call))
    val tileReactionGame = BaseTile(4,R.drawable.ic_reaction_game,resources.getString(R.string.reaction_game))
    val tileMemoryGame = BaseTile(5,R.drawable.ic_memory_game,resources.getString(R.string.memory_game))
    return listOf(tileMemoryLane,tileTODO,tileNotes,tileEmergency,tileReactionGame,tileMemoryGame)
}
