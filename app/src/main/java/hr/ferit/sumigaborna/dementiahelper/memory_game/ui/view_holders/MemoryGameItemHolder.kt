package hr.ferit.sumigaborna.dementiahelper.memory_game.ui.view_holders


import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_memory_game_item.view.*

@EpoxyModelClass(layout = R.layout.cell_memory_game_item)
abstract class MemoryGameItemHolderModel : EpoxyModelWithHolder<MemoryGameItemHolder>(){
    @EpoxyAttribute
    lateinit var allTimeAverage : String
    @EpoxyAttribute
    lateinit var allTimeScoreAverage : String
    @EpoxyAttribute
    lateinit var dateAndDay : String
    @EpoxyAttribute
    lateinit var averageTime : String
    @EpoxyAttribute
    lateinit var score : String

    override fun bind(holder: MemoryGameItemHolder) {
        holder.apply {
            tvMemoryGameItemDate.text = dateAndDay
            tvAverageTime.text = averageTime.substring(0,5)
            if (allTimeAverage.substring(0,5).toFloat()>=averageTime.substring(0,5).toFloat()) tvAverageTime.setTextColor(Color.GREEN)
            else tvAverageTime.setTextColor(Color.RED)
            tvScoreTime.text = score.toInt().toString()
            if (allTimeScoreAverage.toFloat()<=score.toFloat()) tvScoreTime.setTextColor(Color.GREEN)
            else tvScoreTime.setTextColor(Color.RED)
        }
    }
}

class MemoryGameItemHolder : EpoxyHolder(){
    lateinit var tvMemoryGameItemDate : TextView
    lateinit var tvAverageTime : TextView
    lateinit var tvScoreTime : TextView
    override fun bindView(itemView: View) {
        tvMemoryGameItemDate = itemView.tvMemoryGameItemDate
        tvAverageTime = itemView.tvAverageTime
        tvScoreTime = itemView.tvScoreTime
    }
}