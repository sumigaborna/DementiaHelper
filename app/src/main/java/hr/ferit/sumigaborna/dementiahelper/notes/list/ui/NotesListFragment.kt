package hr.ferit.sumigaborna.dementiahelper.notes.list.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.BuildConfig
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.notes.list.view_holders.notesItemHolder
import hr.ferit.sumigaborna.dementiahelper.notes.single.ui.NotesSingleFragment
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesListItem
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesListUI
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesVM
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class NotesListFragment : BaseFragment(),
    NotesListListener {
    override fun getLayout(): Int = R.layout.fragment_notes_list
    private val viewModel by viewModel<NotesVM>()
    private val controller by inject<NotesListFragmentController>{ parametersOf(this)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.notesListData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            hideProgressBar(pbNotesList)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        showProgressBar(pbNotesList)
        rvNotesList.setController(controller)
        viewModel.getAllNotes(activity!!.bottomNav.selectedItemId)
        fbAddNotes.setOnClickListener {
            if(viewModel.notesListData.value!!.notesList.find { it.dateAndDay.substring(0,10) == DateTime.now().toString().substring(0,10) }==null) {
                activity!!.showFragment(
                    R.id.fragmentContainer,
                    NotesSingleFragment(),
                    true,
                    Bundle.EMPTY
                )
            }
            else {
                val bundle = Bundle()
                bundle.putString(KEY_DATE_AND_DAY,viewModel.notesListData.value!!.notesList.find { it.dateAndDay.substring(0,10) == DateTime.now().toString().substring(0,10) }!!.dateAndDay)
                activity!!.showFragment(R.id.fragmentContainer,NotesSingleFragment(),true, bundle)
            }
        }
    }

    override fun openNotesByDateAndDay(dateAndDay: String) {
        val bundle = Bundle()
        bundle.putString(KEY_DATE_AND_DAY,dateAndDay)
        activity!!.showFragment(R.id.fragmentContainer,
            NotesSingleFragment(),true,bundle)
    }

    private fun buildDeleteAlertDialog(context: Context, dateAndDay: String): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete a report?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Delete") { dialog, whichButton ->
                viewModel.deleteDateAndDayItem(activity!!.bottomNav.selectedItemId,dateAndDay)
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
            .create()
    }

    override fun exportPDF(notesListItem: NotesListItem) {
        verifyStoragePermissions(activity!!)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(1200,2010,1).create()
        val page = pdfDocument.startPage(pageInfo)
        fillPdfCanvas(notesListItem,page.canvas)
        pdfDocument.finishPage(page)
        val file = File(context!!.getExternalFilesDir(null),"/Dementia_helper_report_${Calendar.getInstance().timeInMillis}.pdf")
        val fileOutputStream = FileOutputStream(file)
        try {
            pdfDocument.writeTo(fileOutputStream)
        }
        catch (e:IOException){
            toastMessage("Failed to save PDF")
            e.printStackTrace()
        }
        finally {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID + ".provider",file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.setDataAndType(uri, "application/pdf")
            val intent1 = Intent.createChooser(intent, "Open With")
            activity!!.startActivity(intent1)
            pdfDocument.close()
            fileOutputStream.close()
        }
    }

    private fun fillPdfCanvas(notesListItem: NotesListItem, canvas: Canvas) {
        val sixteenDP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16f,resources.displayMetrics)
        val thirtytwoDP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16f,resources.displayMetrics)
        val titlePaint = Paint()
        titlePaint.textAlign = Paint.Align.CENTER
        titlePaint.textSize = 50f
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
        canvas.drawText("Dementia Helper App Report",canvas.width/2f,150f,titlePaint)
        val datePaint = Paint()
        datePaint.textSize = 36f
        canvas.drawText("Date: ${notesListItem.dateAndDay}",sixteenDP,250f,datePaint)
        canvas.drawText("Notes:",sixteenDP,350f,datePaint)
        var height = 350f
        val circlePaint = Paint()
        circlePaint.color = Color.BLACK
        notesListItem.notesList.forEach {
            height+=150
            canvas.drawCircle(thirtytwoDP+20,height-10,3f,circlePaint)
            canvas.drawText(it.note,thirtytwoDP*2,height,datePaint)
        }
    }

    override fun deleteItemByDate(dateAndDay: String) = buildDeleteAlertDialog(activity!!,dateAndDay).show()

}


interface NotesListListener{
    fun openNotesByDateAndDay(dateAndDay:String)
    fun exportPDF(notesListItem: NotesListItem)
    fun deleteItemByDate(dateAndDay: String)
}

class NotesListFragmentController(private val listener : NotesListListener) : TypedEpoxyController<NotesListUI>(){
    override fun buildModels(data: NotesListUI) {
        data.notesList.forEach {
            notesItemHolder {
                id(it.id)
                dateAndDay(it.dateAndDay)
                itemListener { model, parentView, clickedView, position ->
                    listener.openNotesByDateAndDay(it.dateAndDay)
                }
                buttonPdfItem { model, parentView, clickedView, position ->
                    listener.exportPDF(it)
                }
                buttonDeleteItem { model, parentView, clickedView, position ->
                    listener.deleteItemByDate(it.dateAndDay)
                }
            }
        }
    }
}