package hr.ferit.sumigaborna.dementiahelper.notes.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.getTodaysName
import org.joda.time.DateTime

class NotesVM(private val firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore) : BaseViewModel(){

    val notesListData = MutableLiveData<NotesListUI>()
    val notesSingleData = MutableLiveData<NotesSingleUI>()

    init {
        notesSingleData.value = NotesSingleUI(EMPTY_STRING, mutableListOf())
    }

    fun getAllNotes(selectedMenuItem:Int) {
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        if (collectionName.isNotBlank())
        firebaseFirestore.collection(collectionName+"-"+firebaseAuth.currentUser!!.email)
            .get()
            .addOnSuccessListener {
                notesListData.value = provideNotesListUI(it)
            }
            .addOnFailureListener{
                Log.d("ragetag","Failed getting notes: ${it.localizedMessage}")
            }
    }

    fun getNoteByDate(selectedMenuItem: Int,dateAndDay:String){
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        if(collectionName.isNotBlank())
        firebaseFirestore.collection(collectionName+"-"+firebaseAuth.currentUser!!.email)
            .document(dateAndDay)
            .get()
            .addOnSuccessListener {
                if(it.data!=null)
                notesSingleData.value = provideNotesSingleUI(it)
            }
            .addOnFailureListener {
                Log.d("ragetag","Error getting note by date in NotesVM, error message: ${it.localizedMessage}")
            }
    }

    fun saveNotesSingle(selectedMenuItem:Int, note:String){
        val temp = notesSingleData.value!!
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        if(collectionName.isNotBlank()){
            temp.dateAndDay = DateTime.now().toString().substring(0,10)+" ${getTodaysName()}"
            temp.notesList.add(NotesItem(temp.notesList.size,note))
            firebaseFirestore.collection(collectionName+"-"+firebaseAuth.currentUser!!.email.toString())
                .document(temp.dateAndDay)
                .set(temp)
                .addOnSuccessListener {
                    notesSingleData.value = temp
                }
                .addOnFailureListener {
                    Log.d("ragetag","NotesVM: Failed to save a single note: ${it.localizedMessage}")
                }
        }
    }

    private fun getCollectionNameBySelectedMenuItem(selectedMenuItem:Int): String {
        var collectionName = EMPTY_STRING
        when (selectedMenuItem) {
            R.id.patientNavButton -> collectionName = "notes-patient"
            R.id.carerNavButton -> collectionName = "notes-carer"
            else -> Log.d("ragetag","Selected index out of range: NotesSingleFragment->NotesVM")
        }
        return collectionName
    }

    fun deleteNoteById(selectedMenuItem: Int,id: Int) {
        val temp = notesSingleData.value!!
        temp.notesList.remove(temp.notesList.find { it.id == id })
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        if (collectionName.isNotBlank()) {
            firebaseFirestore.collection(collectionName + "-" + firebaseAuth.currentUser!!.email.toString())
                .document(temp.dateAndDay)
                .update("notesList",temp.notesList)
                .addOnSuccessListener {
                    notesSingleData.value = temp
                }
                .addOnFailureListener {
                    notesSingleData.value = notesSingleData.value
                    Log.d("ragetag","Failed to remove single note at NotesVM, additional message: ${it.localizedMessage}")
                }
        }
    }

    fun deleteDateAndDayItem(selectedMenuItem:Int,dateAndDay: String) {
        val temp = notesListData.value!!
        temp.notesList.remove(temp.notesList.find { it.dateAndDay == dateAndDay })
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        if (collectionName.isNotBlank()) {
            firebaseFirestore.collection(collectionName + "-" + firebaseAuth.currentUser!!.email.toString())
                .document(dateAndDay)
                .delete()
                .addOnSuccessListener {
                    notesListData.value = temp
                }
                .addOnFailureListener {
                    notesListData.value = notesListData.value
                    Log.d("ragetag","Failed to remove document by date and day at NotesVM, additional message: ${it.localizedMessage}")
                }
        }
    }

    fun editNote(selectedMenuItem: Int,dateAndDay: String, id: Int, editNote: String) {
        val temp = notesSingleData.value!!
        val noteItem = temp.notesList.find { it.id == id }!!
        noteItem.note = editNote
        val collectionName = getCollectionNameBySelectedMenuItem(selectedMenuItem)
        firebaseFirestore.collection(collectionName + "-" + firebaseAuth.currentUser!!.email.toString())
            .document(dateAndDay)
            .update("notesList",temp.notesList)
            .addOnSuccessListener {
                notesSingleData.value = temp
            }
            .addOnFailureListener {
                notesSingleData.value = notesSingleData.value
                Log.d("ragetag","Failed to edit single note at NotesVM, additional message: ${it.localizedMessage}")
            }
    }

    fun resetNotesSingleData() {
        val temp = notesSingleData.value!!
        temp.dateAndDay = EMPTY_STRING
        temp.notesList.clear()
        notesSingleData.value = temp
    }
}