package hr.ferit.sumigaborna.dementiahelper.profile.data

import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING

data class ProfileUI(
    var userEmail: String = EMPTY_STRING,
    val patientInfo : PatientInfoData,
    val carerInfo : CarerInfoData
)

data class PatientInfoData(
    var patientName : String = EMPTY_STRING,
    var patientSurname : String = EMPTY_STRING,
    var patientAddress: String = EMPTY_STRING,
    var patientAge : String = EMPTY_STRING,
    var patientCity : String = EMPTY_STRING
)

data class CarerInfoData(
    var carerName: String = EMPTY_STRING,
    var carerSurname: String = EMPTY_STRING,
    var carerAddress: String = EMPTY_STRING,
    var carerPhone: String = EMPTY_STRING,
    var carerEmail: String = EMPTY_STRING,
    var carerPassword:String = EMPTY_STRING
)

data class FirebaseResponse(
    var success : Boolean = false,
    var message : String = EMPTY_STRING
)