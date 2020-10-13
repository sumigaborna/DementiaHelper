package hr.ferit.sumigaborna.dementiahelper.todo.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders.categoryHolder
import hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders.taskButtonAndEditTextHolder
import hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders.taskHolder
import hr.ferit.sumigaborna.dementiahelper.todo.view_model.TodoUI
import hr.ferit.sumigaborna.dementiahelper.todo.view_model.TodoVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TodoFragment : BaseFragment(),TodoFragmentListener{
    override fun getLayout(): Int = R.layout.fragment_todo
    private val viewModel by viewModel<TodoVM>()
    private val controller by inject<TodoFragmentController>{ parametersOf(this)}
    private var selectedItemId = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.todoData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            hideProgressBar(pbTodo)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initEpoxy()
    }

    private fun initUI() {
        selectedItemId = activity!!.bottomNav.selectedItemId
        showProgressBar(pbTodo)
        viewModel.getTasks(selectedItemId)
    }

    private fun initEpoxy() = rvTodo.setController(controller)

    override fun openEditDialog(id: Int,isTaskDone: Boolean) {
        val edittext = EditText(activity!!)
        AlertDialog.Builder(activity!!)
            .setTitle("Enter a new note")
            .setView(edittext)
            .setPositiveButton("Change"){ dialog, whichButton ->
                viewModel.editNote(
                    selectedItemId,
                    id,
                    isTaskDone,
                    edittext.text.toString()
                )
            }
            .setNegativeButton("Cancel") { dialog, whichButton ->
                dialog.dismiss()
            }
            .show()
        if(isTaskDone) edittext.text = SpannableStringBuilder(viewModel.todoData.value!!.tasksDone.find { it.id == id }!!.taskText)
        else edittext.text = SpannableStringBuilder(viewModel.todoData.value!!.tasksTodo.find { it.id == id }!!.taskText)
    }

    override fun addNewTask(taskText: String) = viewModel.addNewTask(selectedItemId,taskText)

    override fun markTaskDone(id: Int) = viewModel.markTaskDone(selectedItemId,id)

    override fun markTaskNotDone(id: Int) = viewModel.markTaskNotDone(selectedItemId,id)

    override fun deleteTask(id: Int,isTaskDone: Boolean) {
        AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete a task?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Delete") { dialog, whichButton ->
                viewModel.deleteTask(selectedItemId,isTaskDone,id)
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .show()
    }

    override fun cancelAddingTask() = activity!!.onBackPressed()
}

interface TodoFragmentListener{
    fun openEditDialog(id:Int,isTaskDone: Boolean)
    fun addNewTask(taskText:String)
    fun markTaskDone(id:Int)
    fun markTaskNotDone(id:Int)
    fun deleteTask(id:Int,isTaskDone: Boolean)
    fun cancelAddingTask()
}

class TodoFragmentController(private val listener: TodoFragmentListener): TypedEpoxyController<TodoUI>(){
    override fun buildModels(data: TodoUI) {
        categoryHolder {
            id("category_todo")
            categoryTitle("Not done: ")
        }
        data.tasksTodo.forEach {
            taskHolder {
                id(it.id)
                taskText(it.taskText)
                buttonCheckOrUncheckResource(R.drawable.ic_check)
                taskListener { model, parentView, clickedView, position ->
                    listener.openEditDialog(it.id,it.isTaskDone)
                }
                buttonCheckOrUncheckListener { model, parentView, clickedView, position ->
                    listener.markTaskDone(it.id)
                }
                buttonDeleteListener { model, parentView, clickedView, position ->
                    listener.deleteTask(it.id,it.isTaskDone)
                }
            }
        }
        categoryHolder {
            id("category_done")
            categoryTitle("Done: ")
        }
        data.tasksDone.forEach {
            taskHolder {
                id(it.id)
                taskText(it.taskText)
                buttonCheckOrUncheckResource(R.drawable.ic_clear)
                taskListener { model, parentView, clickedView, position ->
                    listener.openEditDialog(it.id,it.isTaskDone)
                }
                buttonCheckOrUncheckListener { model, parentView, clickedView, position ->
                    listener.markTaskNotDone(it.id)
                }
                buttonDeleteListener { model, parentView, clickedView, position ->
                    listener.deleteTask(it.id,it.isTaskDone)
                }
            }
        }
        taskButtonAndEditTextHolder {
            id("taskButtonAndEditTextHolder")
            buttonAddTaskListener { model, parentView, clickedView, position ->
                listener.addNewTask(parentView.etTask.text.toString())
                parentView.etTask.text.clear()
            }
            buttonCancelListener { model, parentView, clickedView, position ->
                listener.cancelAddingTask()
            }
        }
    }
}