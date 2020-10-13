package hr.ferit.sumigaborna.dementiahelper.notes.single.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.KEY_DATE_AND_DAY
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.notes.single.view_holders.noteItemHolder
import hr.ferit.sumigaborna.dementiahelper.notes.single.view_holders.noteSingleButtonAndEditTextHolder
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesSingleUI
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
import kotlinx.android.synthetic.main.fragment_notes_single.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf


class NotesSingleFragment() : BaseFragment(),NotesSingleListener{

    override fun getLayout(): Int = R.layout.fragment_notes_single
    private val viewModel by sharedViewModel<NotesVM>()
    private val controller by inject<NotesSingleFragmentController>{ parametersOf(this)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.notesSingleData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            hideProgressBar(pbNotesSingle)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initEpoxy()
    }

    private fun initUI() {
        viewModel.resetNotesSingleData()
        showProgressBar(pbNotesSingle)
        if(this.arguments != Bundle.EMPTY)
            viewModel.getNoteByDate(activity!!.bottomNav.selectedItemId,this.arguments!!.get(KEY_DATE_AND_DAY).toString())
    }

    private fun initEpoxy() = rvNotesSingle.setController(controller)

    override fun addNote(note: String) = viewModel.saveNotesSingle(activity!!.bottomNav.selectedItemId,note)

    override fun deleteNoteById(id: Int) {
        AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete a note?")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Delete") { dialog, whichButton ->
                    viewModel.deleteNoteById(activity!!.bottomNav.selectedItemId,id)
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .show()
    }

    override fun openDialogEdit(id: Int) {
        val edittext = EditText(activity!!)
        AlertDialog.Builder(activity!!)
            .setTitle("Enter a new note")
            .setView(edittext)
            .setPositiveButton("Change"){ dialog, whichButton ->
                viewModel.editNote(
                    activity!!.bottomNav.selectedItemId,
                    this.arguments?.getString(KEY_DATE_AND_DAY).toString(),
                    id,
                    edittext.text.toString()
                )
            }
            .setNegativeButton("Cancel") { dialog, whichButton ->
                dialog.dismiss()
            }
            .show()
        edittext.text = SpannableStringBuilder(viewModel.notesSingleData.value!!.notesList.find { it.id == id }!!.note)
    }

    override fun onCancelPress() = activity!!.onBackPressed()
}

interface NotesSingleListener{
    fun addNote(note:String)
    fun deleteNoteById(id:Int)
    fun openDialogEdit(id:Int)
    fun onCancelPress()
}

class NotesSingleFragmentController(private val listener:NotesSingleListener) : TypedEpoxyController<NotesSingleUI>(){
    override fun buildModels(data: NotesSingleUI) {
        data.notesList.forEach {
            noteItemHolder {
                id(it.id)
                note(it.note)
                tvNoteEditListener { model, parentView, clickedView, position ->
                    listener.openDialogEdit(it.id)
                }
                buttonDeleteListener { model, parentView, clickedView, position ->
                    listener.deleteNoteById(it.id)
                }
            }
        }
        if(data.dateAndDay.isBlank()||data.dateAndDay.substring(0,10)==DateTime.now().toString().substring(0,10))
        noteSingleButtonAndEditTextHolder {
            id(data.hashCode())
            buttonAddNoteListener { model, parentView, clickedView, position ->
                listener.addNote(parentView.etNote.text.toString())
                parentView.etNote.text.clear()
            }
            buttonCancelListener { model, parentView, clickedView, position ->
                listener.onCancelPress()
            }
        }
    }
}

