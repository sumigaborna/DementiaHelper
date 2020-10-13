package hr.ferit.sumigaborna.dementiahelper.notes.single.view_holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_notes_single_item.view.*

@EpoxyModelClass(layout = R.layout.cell_notes_single_item)
abstract class NoteItemHolderModel : EpoxyModelWithHolder<NoteItemHolder>(){
    @EpoxyAttribute lateinit var note : String
    @EpoxyAttribute(hash = false) lateinit var buttonDeleteListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var tvNoteEditListener : View.OnClickListener
    override fun bind(holder: NoteItemHolder) {
        holder.apply {
            tvNote.text = note
            tvNote.setOnClickListener(tvNoteEditListener)
            buttonDeleteNote.setOnClickListener(buttonDeleteListener)
        }
    }
}

class NoteItemHolder : EpoxyHolder(){
    lateinit var tvNote : TextView
    lateinit var buttonDeleteNote : Button
    override fun bindView(itemView: View) {
        tvNote = itemView.tvNoteSingle
        buttonDeleteNote = itemView.buttonDeleteSingleNote
    }
}