package hr.ferit.sumigaborna.dementiahelper.carer.view_model

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.data.BaseTile
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.carer.data.CarerUI
import hr.ferit.sumigaborna.dementiahelper.carer.data.provideCarerPassword

class CarerVM(private val firebaseFirestore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth, resources: Resources): BaseViewModel(){

    val carerLiveData = MutableLiveData<CarerUI>()
    var carerPassword = EMPTY_STRING
    val carerFailureData = MutableLiveData<String>()

    init {
        val tileTODO = BaseTile(0, R.drawable.ic_todo,resources.getString(R.string.todos))
        val tileNotes = BaseTile(1, R.drawable.ic_notes,resources.getString(R.string.notes))
        val tileReminder = BaseTile(2, R.drawable.ic_reminder,resources.getString(R.string.reminder))
        val tileEmergency = BaseTile(3, R.drawable.ic_emergency_call,resources.getString(R.string.emergency_call))
        carerLiveData.value = CarerUI(listOf(tileTODO,tileNotes,tileReminder,tileEmergency))
    }

    fun getCarerPassword(){
        firebaseFirestore.collection("users")
            .document(firebaseAuth.currentUser!!.email!!)
            .get()
            .addOnSuccessListener {
                carerPassword = provideCarerPassword(it)
            }
            .addOnFailureListener {
                carerFailureData.value = "Failed to get carer password"
            }
    }
}