package hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_task_edittext_and_button.view.*

@EpoxyModelClass(layout = R.layout.cell_task_edittext_and_button)
abstract class TaskButtonAndEditTextHolderModel:EpoxyModelWithHolder<TaskButtonAndEditTextHolder>(){
    @EpoxyAttribute(hash = false) lateinit var buttonAddTaskListener : View.OnClickListener
    @EpoxyAttribute(hash = false) lateinit var buttonCancelListener : View.OnClickListener
    override fun bind(holder: TaskButtonAndEditTextHolder) {
        holder.apply {
            buttonAddTask.setOnClickListener(buttonAddTaskListener)
            buttonCancel.setOnClickListener(buttonCancelListener)
        }
    }
}

class TaskButtonAndEditTextHolder :EpoxyHolder(){
    lateinit var etTask : EditText
    lateinit var buttonAddTask : Button
    lateinit var buttonCancel : Button
    override fun bindView(itemView: View) {
        etTask = itemView.etAddTask
        buttonAddTask = itemView.buttonSaveTask
        buttonCancel = itemView.buttonCancel
    }
}