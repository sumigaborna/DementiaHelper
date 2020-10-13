package hr.ferit.sumigaborna.dementiahelper.notes.single.view_holders

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_notes_single_edittext_and_button.view.*

@EpoxyModelClass(layout = R.layout.cell_notes_single_edittext_and_button)
abstract class NoteSingleButtonAndEditTextHolderModel:EpoxyModelWithHolder<NoteSingleButtonAndEditTextHolder>(){
    @EpoxyAttribute(hash = false) lateinit var buttonAddNoteListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonCancelListener : View.OnClickListener
    override fun bind(holder: NoteSingleButtonAndEditTextHolder) {
        holder.apply {
            buttonAddNote.setOnClickListener(buttonAddNoteListener)
            buttonCancel.setOnClickListener(buttonCancelListener)
        }
    }
}

class NoteSingleButtonAndEditTextHolder :EpoxyHolder(){
    lateinit var etNote : EditText
    lateinit var buttonAddNote : Button
    lateinit var buttonCancel : Button
    override fun bindView(itemView: View) {
        etNote = itemView.etAddNote
        buttonAddNote = itemView.buttonSaveNote
        buttonCancel = itemView.buttonCancel
    }
}