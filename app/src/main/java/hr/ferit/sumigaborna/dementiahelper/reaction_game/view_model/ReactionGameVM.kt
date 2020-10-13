package hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.getTodaysName
import org.joda.time.DateTime

class ReactionGameVM(private val firebaseAuth: FirebaseAuth,private val firebaseFirestore: FirebaseFirestore) : BaseViewModel(){
    val reactionGameData = MutableLiveData<ReactionGameUI>()
    val reactionGameUpload = MutableLiveData<String>()
    val reactionGameGet = MutableLiveData<String>()

    fun uploadGameReport(report: ReactionGameResult) {
        val temp = reactionGameData.value!!
        report.id = getMaxId(temp.listResult)
        report.dateAndDay = DateTime.now().toString().substring(0,10)+" ${getTodaysName()}"
        temp.listResult.add(report)
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email!!)
            .document("Reaction Game Results")
            .set(temp)
            .addOnSuccessListener {
                reactionGameData.value = temp
            }
            .addOnFailureListener {
                reactionGameUpload.value = "Failed to upload game report"
            }
    }

    private fun getMaxId(results: MutableList<ReactionGameResult>): Int {
        var maxId = 0
        results.forEach {
            if(it.id>=maxId) maxId = it.id+1
        }
        return maxId
    }

    fun getReports() {
        firebaseFirestore.collection(firebaseAuth.currentUser!!.email!!)
            .document("Reaction Game Results")
            .get()
            .addOnSuccessListener {
                reactionGameData.value = provideReactionGameUI(it)
            }
            .addOnFailureListener {
                reactionGameGet.value = "Failed to get game reports"
            }
    }
}