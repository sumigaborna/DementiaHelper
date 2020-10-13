package hr.ferit.sumigaborna.dementiahelper.reminder.ui.view_holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_notes_single_item.view.*
import kotlinx.android.synthetic.main.cell_reminder_item.view.*

@EpoxyModelClass(layout = R.layout.cell_reminder_item)
abstract class ReminderItemHolderModel : EpoxyModelWithHolder<ReminderItemHolder>(){
    @EpoxyAttribute lateinit var reminder : String
    @EpoxyAttribute(hash = false) lateinit var buttonDeleteListener : View.OnClickListener
    override fun bind(holder: ReminderItemHolder) {
        holder.apply {
            tvReminder.text = reminder
            buttonDeleteReminder.setOnClickListener(buttonDeleteListener)
        }
    }
}

class ReminderItemHolder : EpoxyHolder(){
    lateinit var tvReminder : TextView
    lateinit var buttonDeleteReminder : Button
    override fun bindView(itemView: View) {
        tvReminder = itemView.tvReminder
        buttonDeleteReminder = itemView.buttonDeleteReminder
    }
}