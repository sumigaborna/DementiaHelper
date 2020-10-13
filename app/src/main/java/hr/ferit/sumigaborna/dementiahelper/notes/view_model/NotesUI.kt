@file:Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")

package hr.ferit.sumigaborna.dementiahelper.notes.view_model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING

data class NotesListUI(val notesList:MutableList<NotesListItem>)

data class NotesListItem(val id: Int, val dateAndDay: String,val notesList: MutableList<NotesItem>)

data class NotesSingleUI(var dateAndDay:String,val notesList: MutableList<NotesItem>)

data class NotesItem(val id:Int, var note:String)

fun provideNotesListUI(querySnapshot: QuerySnapshot):NotesListUI{
    val list = mutableListOf<NotesListItem>()
    querySnapshot.documents.forEachIndexed { index, it ->
        list.add(NotesListItem(index,it.id,mapListDataToMutableList(it.data!!.getValue("notesList"))))
    }
    return NotesListUI(list)
}

fun provideNotesSingleUI(documentSnapshot: DocumentSnapshot):NotesSingleUI{
    val dateAndDay = documentSnapshot.data!!.getValue("dateAndDay")
    val list = documentSnapshot.data!!.getValue("notesList")
    return NotesSingleUI(dateAndDay.toString(), mapListDataToMutableList(list))
}

fun mapListDataToMutableList(list: Any): MutableList<NotesItem> {
    val returnList = mutableListOf<NotesItem>()
    (list as List<HashMap<String,Any>>).forEach {
        returnList.add(
            NotesItem(
                id = it.getValue("id").toString().toInt(),
                note = it.getValue("note").toString()
            )
        )
    }
    return returnList
}

