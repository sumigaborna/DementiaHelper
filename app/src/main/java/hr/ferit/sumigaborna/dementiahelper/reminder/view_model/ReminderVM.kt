package hr.ferit.sumigaborna.dementiahelper.reminder.view_model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.ALARM_NOTE
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.REMINDERS
import hr.ferit.sumigaborna.dementiahelper.app.services.AlarmReceiver
import java.util.*

class ReminderVM(private val context: Context, private val firebaseFirestore: FirebaseFirestore,private val firebaseAuth: FirebaseAuth, private val alarmManager: AlarmManager) :BaseViewModel(){

    val reminderData = MutableLiveData<ReminderUI>()
    val reminderFailureMessage = MutableLiveData<String>()
    private lateinit var pendingIntent : PendingIntent

    init {
        reminderFailureMessage.value = EMPTY_STRING
        reminderData.value = ReminderUI()
    }

    fun getReminders() {
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email.toString())
            .document(REMINDERS)
            .get()
            .addOnSuccessListener {
                reminderData.value = provideReminderUI(it)
            }
            .addOnFailureListener {
                reminderFailureMessage.value = "Failed to get reminders :("
            }
    }


    fun saveReminder(calendar:Calendar,date:String,time:String,note:String){
        val temp = reminderData.value!!
        temp.reminders.add(
            ReminderItem(
                getMaxId(temp.reminders),
                date,
                time,
                note,
                "$note at $date $time"
            )
        )
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email.toString())
            .document(REMINDERS)
            .set(temp)
            .addOnSuccessListener {
                reminderData.value = temp
                pendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                    intent.putExtra(ALARM_NOTE,temp.reminders.last().note)
                    PendingIntent.getBroadcast(context, getMaxId(temp.reminders), intent, 0)
                }
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
            }
            .addOnFailureListener {
                reminderFailureMessage.value = "Failed to save reminder"
            }
    }

    private fun getMaxId(reminders: MutableList<ReminderItem>): Int {
        var maxId = 0
        reminders.forEach {
            if(it.id>=maxId) maxId = it.id+1
        }
        return maxId
    }

    fun deleteReminder(id: Int) {
        val temp = reminderData.value!!
        temp.reminders.remove(temp.reminders.find { it.id == id })
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email.toString())
            .document(REMINDERS)
            .set(temp)
            .addOnSuccessListener {
                reminderData.value = temp
                pendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(context, id, intent, 0)
                }
                alarmManager.cancel(pendingIntent)
            }
            .addOnFailureListener {
                reminderFailureMessage.value = "Failed to delete reminder"
            }
    }
}