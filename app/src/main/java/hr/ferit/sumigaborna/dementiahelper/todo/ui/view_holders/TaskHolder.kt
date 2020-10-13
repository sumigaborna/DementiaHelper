package hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_task.view.*

@EpoxyModelClass(layout = R.layout.cell_task)
abstract class TaskHolderModel : EpoxyModelWithHolder<TaskHolder>(){
    @EpoxyAttribute lateinit var taskText : String
    @EpoxyAttribute var buttonCheckOrUncheckResource : Int = 0
    @EpoxyAttribute(hash = false) lateinit var taskListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonCheckOrUncheckListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonDeleteListener : View.OnClickListener
    override fun bind(holder: TaskHolder) {
        holder.apply {
            tvTask.text = taskText
            buttonCheckOrUncheck.setBackgroundResource(buttonCheckOrUncheckResource)
            tvTask.setOnClickListener(taskListener)
            buttonCheckOrUncheck.setOnClickListener(buttonCheckOrUncheckListener)
            buttonDelete.setOnClickListener(buttonDeleteListener)
        }
    }
}

class TaskHolder : EpoxyHolder(){
    lateinit var tvTask : TextView
    lateinit var buttonCheckOrUncheck : Button
    lateinit var buttonDelete : Button
    override fun bindView(itemView: View) {
        tvTask = itemView.tvTask
        buttonCheckOrUncheck = itemView.buttonCheckOrUncheck
        buttonDelete = itemView.buttonDeleteTask
    }
}