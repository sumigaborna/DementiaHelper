package hr.ferit.sumigaborna.dementiahelper.reminder.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.reminder.ui.view_holders.reminderCreateHolder
import hr.ferit.sumigaborna.dementiahelper.reminder.ui.view_holders.reminderItemHolder
import hr.ferit.sumigaborna.dementiahelper.reminder.view_model.ReminderUI
import hr.ferit.sumigaborna.dementiahelper.reminder.view_model.ReminderVM
import kotlinx.android.synthetic.main.fragment_reminder.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ReminderFragment : BaseFragment(),ReminderFragmentListener{

    override fun getLayout(): Int = R.layout.fragment_reminder
    private val viewModel by viewModel<ReminderVM>()
    private val controller by inject<ReminderFragmentController>{ parametersOf(this)}
    private val calendar = Calendar.getInstance()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.reminderData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            controller.setData(it)
            rvReminder.scrollToPosition(0)
            if(this.isResumed)hideProgressBar(pbReminder)
        })
        viewModel.reminderFailureMessage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it.isNotBlank()) {
                toastMessage(it)
                hideProgressBar(pbReminder)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){
        rvReminder.setController(controller)
        showProgressBar(pbReminder)
        viewModel.getReminders()
    }

    override fun deleteReminder(id: Int) = viewModel.deleteReminder(id)

    override fun showDateDialog(editText: EditText) {
        val datePickerDialog = DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year,month,dayOfMonth)
                editText.text = SpannableStringBuilder(localizeDate(calendar.toInstant().toString()))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun showTimeDialog(editText: EditText) {
        TimePickerDialog(activity!!,android.R.style.Theme_Holo_Light_Dialog,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),hourOfDay, minute,0)
                editText.text = SpannableStringBuilder(String.format("%02d:%02d",hourOfDay, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
            ).show()
    }

    override fun saveReminder(
        etReminderDate: EditText,
        etReminderTime: EditText,
        etReminderNote: EditText
    ) {
        if(etReminderDate.text.isEmpty() || etReminderTime.text.isEmpty() || etReminderNote.text.isBlank())
            toastMessage("Please enter all inputs before saving!")
        else {
            (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view!!.windowToken,0)
            viewModel.saveReminder(calendar,etReminderDate.text.toString(),etReminderTime.text.toString(),etReminderNote.text.toString())
        }
    }

    override fun cancel() = activity!!.onBackPressed()
}

interface ReminderFragmentListener{
    fun deleteReminder(id:Int)
    fun showDateDialog(editText: EditText)
    fun showTimeDialog(editText: EditText)
    fun saveReminder(etReminderDate:EditText,etReminderTime:EditText,etReminderNote:EditText)
    fun cancel()
}

class ReminderFragmentController(private val listener: ReminderFragmentListener):TypedEpoxyController<ReminderUI>(){
    override fun buildModels(data: ReminderUI) {
        data.reminders.forEach {
            reminderItemHolder {
                id(it.id)
                reminder(it.reminder)
                buttonDeleteListener { model, parentView, clickedView, position ->
                    listener.deleteReminder(it.id)
                }
            }
        }
        reminderCreateHolder {
            id("reminder_create_holder")
            etDateListener { model, parentView, clickedView, position ->
                listener.showDateDialog(parentView.etReminderDate)
            }
            etTimeListener { model, parentView, clickedView, position ->
                listener.showTimeDialog(parentView.etReminderTime)
            }
            buttonSaveListener { model, parentView, clickedView, position ->
                listener.saveReminder(parentView.etReminderDate,parentView.etReminderTime,parentView.etReminderNote)
            }
            buttonCancelListener { model, parentView, clickedView, position ->
                listener.cancel()
            }
        }
    }
}