package hr.ferit.sumigaborna.dementiahelper.reaction_game.list

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_reaction_game_item.view.*

@EpoxyModelClass(layout = R.layout.cell_reaction_game_item)
abstract class ReactionGameItemHolderModel : EpoxyModelWithHolder<ReactionGameItemHolder>(){
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

    override fun bind(holder: ReactionGameItemHolder) {
        holder.apply {
            tvReportGameItemDate.text = dateAndDay
            tvAverageTime.text = averageTime.substring(0,5).takeIf { it.length>4 }
            if (allTimeAverage.substring(0,3).toFloat()>=averageTime.substring(0,3).toFloat()) tvAverageTime.setTextColor(Color.GREEN)
            else tvAverageTime.setTextColor(Color.RED)
            tvScoreTime.text = score.toInt().toString()
            if (allTimeScoreAverage.toFloat()<=score.toFloat()) tvScoreTime.setTextColor(Color.GREEN)
            else tvScoreTime.setTextColor(Color.RED)
        }
    }
}

class ReactionGameItemHolder : EpoxyHolder(){
    lateinit var tvReportGameItemDate : TextView
    lateinit var tvAverageTime : TextView
    lateinit var tvScoreTime : TextView
    override fun bindView(itemView: View) {
        tvReportGameItemDate = itemView.tvReactionGameItemDate
        tvAverageTime = itemView.tvAverageTime
        tvScoreTime = itemView.tvScoreTime
    }
}