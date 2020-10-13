package hr.ferit.sumigaborna.dementiahelper.notes.list.view_holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_notes_item.view.*

@EpoxyModelClass(layout = R.layout.cell_notes_item)
abstract class NotesItemHolderModel : EpoxyModelWithHolder<NotesItemHolder>(){
    @EpoxyAttribute lateinit var dateAndDay : String
    @EpoxyAttribute(hash = false) lateinit var itemListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonDeleteItem : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonPdfItem : View.OnClickListener
    override fun bind(holder: NotesItemHolder) {
        holder.tvDateAndDay.text = dateAndDay
        holder.notesItemView.setOnClickListener(itemListener)
        holder.buttonDeleteItem.setOnClickListener(buttonDeleteItem)
        holder.buttonPdfItem.setOnClickListener(buttonPdfItem)
    }
}

class NotesItemHolder : EpoxyHolder(){
    lateinit var tvDateAndDay : TextView
    lateinit var notesItemView : View
    lateinit var buttonDeleteItem : Button
    lateinit var buttonPdfItem : Button
    override fun bindView(itemView: View) {
        tvDateAndDay = itemView.tvNotesItemDateAndDay
        notesItemView = itemView
        buttonDeleteItem = itemView.buttonDeleteNotesListItem
        buttonPdfItem = itemView.buttonPDFNotesListItem
    }
}