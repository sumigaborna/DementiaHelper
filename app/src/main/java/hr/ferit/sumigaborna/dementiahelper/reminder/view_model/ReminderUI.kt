package hr.ferit.sumigaborna.dementiahelper.reminder.view_model

import com.google.firebase.firestore.DocumentSnapshot

data class ReminderUI(val reminders : MutableList<ReminderItem> = mutableListOf())

data class ReminderItem(val id:Int, val date:String, val time:String, val note:String, val reminder:String)

fun provideReminderUI(documentSnapshot: DocumentSnapshot): ReminderUI {
    if(documentSnapshot.data != null) {
        val returnList = mutableListOf<ReminderItem>()
        val reminder = documentSnapshot.data!!.getValue("reminders")
        (reminder as List<HashMap<String,Any>>).forEach{
            returnList.add(
                ReminderItem(
                    it.getValue("id").toString().toInt(),
                    it.getValue("date").toString(),
                    it.getValue("time").toString(),
                    it.getValue("note").toString(),
                    "${it.getValue("note")} at ${it.getValue("date")} ${it.getValue("time")}"
                )
            )
        }
        return ReminderUI(returnList)
    }
    else return ReminderUI(mutableListOf())
}