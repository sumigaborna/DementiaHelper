package hr.ferit.sumigaborna.dementiahelper.patient.ui.view_holders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_quotes.view.*

@EpoxyModelClass(layout = R.layout.cell_quotes)
abstract class QuotesHolderModel : EpoxyModelWithHolder<QuotesHolder>(){
    @EpoxyAttribute lateinit var quote : String
    override fun bind(holder: QuotesHolder) {
        holder.tvQuote.text = quote
    }
}

class QuotesHolder : EpoxyHolder(){
    lateinit var tvQuote : TextView
    override fun bindView(itemView: View) {
        tvQuote = itemView.tvQuote
    }
}