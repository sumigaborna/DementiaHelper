package hr.ferit.sumigaborna.dementiahelper.carer.data

import com.google.firebase.firestore.DocumentSnapshot
import hr.ferit.sumigaborna.dementiahelper.app.base.data.BaseTile
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING


data class CarerUI(
    val carerTiles: List<BaseTile>
)

fun provideCarerPassword(documentSnapshot: DocumentSnapshot):String{
    return if(documentSnapshot.data != null){
        val carerInfo = documentSnapshot.data!!.getValue("carerInfo") as HashMap<String,String>
        carerInfo.getValue("carerPassword")
    }
    else EMPTY_STRING
}

