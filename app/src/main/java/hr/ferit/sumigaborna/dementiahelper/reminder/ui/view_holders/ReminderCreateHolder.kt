package hr.ferit.sumigaborna.dementiahelper.reminder.ui.view_holders

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_reminder_create.view.*

@EpoxyModelClass(layout = R.layout.cell_reminder_create)
abstract class ReminderCreateHolderModel : EpoxyModelWithHolder<ReminderCreateHolder>(){
    @EpoxyAttribute(hash = false) lateinit var etDateListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var etTimeListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonSaveListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonCancelListener : View.OnClickListener
    override fun bind(holder: ReminderCreateHolder) {
        holder.apply {
            etReminderDate.setOnClickListener(etDateListener)
            etReminderTime.setOnClickListener(etTimeListener)
            buttonSaveReminder.setOnClickListener(buttonSaveListener)
            buttonCancel.setOnClickListener(buttonCancelListener)
        }
    }
}

class ReminderCreateHolder : EpoxyHolder(){
    lateinit var etReminderDate : EditText
    lateinit var etReminderTime : EditText
    lateinit var etReminderNote : EditText
    lateinit var buttonSaveReminder : Button
    lateinit var buttonCancel : Button
    override fun bindView(itemView: View) {
        etReminderDate = itemView.etReminderDate
        etReminderTime = itemView.etReminderTime
        etReminderNote = itemView.etReminderNote
        buttonSaveReminder = itemView.buttonSaveReminder
        buttonCancel = itemView.buttonCancel
    }
}