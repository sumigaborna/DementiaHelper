package hr.ferit.sumigaborna.dementiahelper.profile.view_model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.carer.data.provideCarerPassword
import hr.ferit.sumigaborna.dementiahelper.profile.data.CarerInfoData
import hr.ferit.sumigaborna.dementiahelper.profile.data.PatientInfoData
import hr.ferit.sumigaborna.dementiahelper.profile.data.ProfileUI
import hr.ferit.sumigaborna.dementiahelper.profile.data.FirebaseResponse

class ProfileVM(private val firebaseAuth: FirebaseAuth, private val firebaseFirestore: FirebaseFirestore) : BaseViewModel(){

    val profileData =  MutableLiveData<ProfileUI>()
    val getProfileInfoResponse = MutableLiveData<FirebaseResponse>()
    val resetLinkResponse = MutableLiveData<FirebaseResponse>()
    val addEditProfileInfoResponse = MutableLiveData<FirebaseResponse>()
    val carerPasswordFailure = MutableLiveData<String>()
    var carerPassword = EMPTY_STRING
    val carerFailureData = MutableLiveData<String>()

    init {
        profileData.value = ProfileUI(EMPTY_STRING, PatientInfoData(), CarerInfoData())
        getProfileInfoResponse.value = FirebaseResponse()
        resetLinkResponse.value = FirebaseResponse()
        addEditProfileInfoResponse.value = FirebaseResponse()
        carerPasswordFailure.value = EMPTY_STRING
    }

    fun getAllProfileInfo() {
        val temp = profileData.value!!
        firebaseFirestore.collection("users")
            .document(firebaseAuth.currentUser!!.email!!)
            .get()
            .addOnSuccessListener {
                profileData.value = mapProfileInfoData(it)
                val response = getProfileInfoResponse.value!!
                response.success = true
                response.message = "Successfully fetched data"
                getProfileInfoResponse.value = response
            }
            .addOnFailureListener {
                val response = getProfileInfoResponse.value!!
                response.success = true
                response.message = "Failed fetching profile information"
                getProfileInfoResponse.value = response
            }
        temp.userEmail = firebaseAuth.currentUser!!.email!!
        profileData.value = temp
    }

    private fun mapProfileInfoData(it:DocumentSnapshot): ProfileUI {
        return if(!it.data.isNullOrEmpty()) {
            val patientData = it.get("patientInfo") as Map<String, String>
            val carerData = it.get("carerInfo") as Map<String, String>
            ProfileUI(
                firebaseAuth.currentUser!!.email!!,
                PatientInfoData(
                    patientData.getValue("patientName"),
                    patientData.getValue("patientSurname"),
                    patientData.getValue("patientAddress"),
                    patientData.getValue("patientAge"),
                    patientData.getValue("patientCity")
                ),
                CarerInfoData(
                    carerData.getValue("carerName"),
                    carerData.getValue("carerSurname"),
                    carerData.getValue("carerAddress"),
                    carerData.getValue("carerPhone"),
                    carerData.getValue("carerEmail")
                )
            )
        }
        else ProfileUI(firebaseAuth.currentUser!!.email!!, PatientInfoData(), CarerInfoData())
    }

    fun requestChangeOfUserPassword(){
        firebaseAuth.sendPasswordResetEmail(firebaseAuth.currentUser!!.email!!)
            .addOnSuccessListener {
                val temp = resetLinkResponse.value!!
                temp.success = true
                temp.message = "Successfully sent reset link to ${firebaseAuth.currentUser!!.email!!}"
                resetLinkResponse.value = temp
            }
            .addOnFailureListener {
                val temp = resetLinkResponse.value!!
                temp.success = false
                temp.message = "Failed to send reset link, please check your internet connection."
                resetLinkResponse.value = temp
            }
    }

    fun logOutUser() = firebaseAuth.signOut()

    fun saveProfileInfo(
        patientName: String,
        patientSurname: String,
        patientAddress: String,
        patientAge: String,
        patientCity: String,
        carerName: String,
        carerSurname: String,
        carerAddress: String,
        carerPhone: String,
        carerEmail: String
    ){
        val info = ProfileUI(
            firebaseAuth.currentUser!!.email!!,
            PatientInfoData(patientName,patientSurname,patientAddress,patientAge,patientCity),
            CarerInfoData(carerName,carerSurname,carerAddress,carerPhone,carerEmail)
        )
        firebaseFirestore.collection("users").document(firebaseAuth.currentUser!!.email!!).set(info)
            .addOnSuccessListener {
                val temp = addEditProfileInfoResponse.value!!
                temp.success = true
                temp.message = "Successfully updated profile info!"
                addEditProfileInfoResponse.value = temp
            }
            .addOnFailureListener {
                val temp = addEditProfileInfoResponse.value!!
                temp.success = false
                temp.message = "Failed to update profile info!"
                addEditProfileInfoResponse.value = temp
            }
    }

    fun resetAddEditProfileResponse() {
        val temp = addEditProfileInfoResponse.value!!
        temp.success = false
        temp.message = ""
        addEditProfileInfoResponse.value = temp
    }

    fun changeCarerPassword(newCarerPassword: String) {
        val temp = profileData.value!!
        temp.carerInfo.carerPassword = newCarerPassword
        firebaseFirestore.collection("users")
            .document(firebaseAuth.currentUser!!.email!!)
            .set(temp)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
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