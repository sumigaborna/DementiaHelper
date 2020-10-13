package hr.ferit.sumigaborna.dementiahelper.memory_game

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.getTodaysName
import org.joda.time.DateTime

class MemoryGameVM(private val firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore) : BaseViewModel(){

    val memoryGameData = MutableLiveData<MemoryGameUI>()
    val memoryGameUpload = MutableLiveData<String>()
    val memoryGameGet = MutableLiveData<String>()

    fun getReports() {
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email!!)
            .document("Memory Game Results")
            .get()
            .addOnSuccessListener {
                memoryGameData.value = provideMemoryGameUI(it)
            }
            .addOnFailureListener {
                memoryGameGet.value = "Failed to get game reports"
            }
    }

    fun uploadMemoryGameResult(report:MemoryGameResult) {
        val temp = memoryGameData.value!!
        report.id = getMaxId(temp.listResult)
        report.dateAndDay = DateTime.now().toString().substring(0,10)+" ${getTodaysName()}"
        temp.listResult.add(report)
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email!!)
            .document("Memory Game Results")
            .set(temp)
            .addOnSuccessListener {
                memoryGameData.value = temp
            }
            .addOnFailureListener {
                memoryGameUpload.value = "Failed to upload game report"
            }
    }

    private fun getMaxId(results: MutableList<MemoryGameResult>): Int {
        var maxId = 0
        results.forEach {
            if(it.id>=maxId) maxId = it.id+1
        }
        return maxId
    }

}