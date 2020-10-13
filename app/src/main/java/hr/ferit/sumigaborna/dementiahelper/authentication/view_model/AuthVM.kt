package hr.ferit.sumigaborna.dementiahelper.authentication.view_model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.DEFAULT_CITY
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.authentication.data.AuthData
import hr.ferit.sumigaborna.dementiahelper.profile.data.CarerInfoData
import hr.ferit.sumigaborna.dementiahelper.profile.data.PatientInfoData
import hr.ferit.sumigaborna.dementiahelper.profile.data.ProfileUI

class AuthVM(private val firebaseAuth: FirebaseAuth, private val firebaseFirestore: FirebaseFirestore) : BaseViewModel(){

    val authData = MutableLiveData<AuthData>()

    init {
        authData.value = AuthData()
    }

    fun registerUser(email:String,password:String,carerPassword: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                saveCarerPassword(carerPassword)
            }
            .addOnFailureListener {
                val temp = authData.value
                temp?.registerSuccess = false
                temp?.registerErrorMessage = it.localizedMessage ?: "Failed registration"
                authData.value = temp
            }
    }

    fun logInUser(email:String,password: String){
        if(!isUserLoggedIn())
            firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                val temp = authData.value
                temp?.loginSuccess = true
                temp?.loginSuccessMessage = "Successful login!"
                authData.value = temp

            }
            .addOnFailureListener {
                val temp = authData.value
                temp?.loginSuccess = false
                temp?.loginErrorMessage = it.localizedMessage ?: "Failed registration"
                authData.value = temp
            }
    }

    private fun isUserLoggedIn():Boolean = firebaseAuth.currentUser!=null

    fun reLogUser() {
        if(isUserLoggedIn()) {
            val temp = authData.value
            temp?.loginSuccess = true
            temp?.loginSuccessMessage = "Successful login!"
            authData.value = temp
        }
        else{
            val temp = authData.value
            temp?.loginSuccess = false
            temp?.loginSuccessMessage = EMPTY_STRING
            authData.value = temp
        }
    }

    private fun saveCarerPassword(carerPassword:String){
        val info = ProfileUI(
            firebaseAuth.currentUser!!.email!!,
            PatientInfoData(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING, DEFAULT_CITY),
            CarerInfoData(EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,carerPassword)
        )

        firebaseFirestore.collection("users")
            .document(firebaseAuth.currentUser!!.email!!)
            .set(info)
            .addOnSuccessListener {
                val temp = authData.value
                temp?.registerSuccess = true
                temp?.registerSuccessMessage = "Successful registration!"
                authData.value = temp
            }
            .addOnFailureListener {
                val temp = authData.value
                temp?.registerSuccess = false
                temp?.registerErrorMessage = it.localizedMessage ?: "Failed registration"
                authData.value = temp
            }
    }
}