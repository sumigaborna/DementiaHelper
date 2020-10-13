package hr.ferit.sumigaborna.dementiahelper.patient.view_model

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.app.base.view_model.BaseViewModel
import hr.ferit.sumigaborna.dementiahelper.app.common.DEFAULT_CITY
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.patient.data.*
import hr.ferit.sumigaborna.dementiahelper.patient.interactor.QuotesInteractor
import hr.ferit.sumigaborna.dementiahelper.patient.interactor.WeatherInteractor
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_patient.*

class PatientVM(
    private val weatherInteractor: WeatherInteractor,
    private val quotesInteractor: QuotesInteractor,
    private val resources: Resources,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): BaseViewModel(){

    val patientLiveData = MutableLiveData<PatientUI>()

    init {
        patientLiveData.value = PatientUI(
            DEFAULT_CITY,
            WeatherInfo(0, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING),
            Quotes(0, emptyList()),
            provideDefaultPatientTiles(resources)
        )
    }

    private fun getWeatherAndQuotes() : Observable<PatientUI> {
        return if(patientLiveData.value!!.weatherInfo.id==0 || patientLiveData.value!!.quotesList.quotes.isEmpty()){
            Observable.zip(
                weatherInteractor.getCityWeather(patientLiveData.value!!.patientCity),
                quotesInteractor.getQuotes(),
                BiFunction{t1:WeatherResponse,t2:QuotesResponse ->
                    providePatientUI(patientLiveData.value!!.patientCity,t1,t2,resources)
                }
            )
                .doOnNext {
                    it.loadingDone = true
                    patientLiveData.value = it }
        }
        else Observable.empty()
    }

    fun getPatientCityWeatherAndQuotes() {
        val temp = patientLiveData.value!!
        firebaseFirestore.collection("users")
            .document(firebaseAuth.currentUser!!.email.toString())
            .get()
            .addOnSuccessListener {
                if(!it.data.isNullOrEmpty()) {
                    val patientData = it.get("patientInfo") as Map<String, String>
                    temp.patientCity = patientData.getValue("patientCity").toString()
                }
                addDisposable(getWeatherAndQuotes().subscribe())
                patientLiveData.value = temp
            }
            .addOnFailureListener {
                Log.d("ragetag","PatientVM failed to get city from Firebase")
            }
    }
}