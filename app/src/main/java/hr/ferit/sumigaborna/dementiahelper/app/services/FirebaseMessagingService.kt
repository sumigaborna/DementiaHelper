package hr.ferit.sumigaborna.dementiahelper.app.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService  : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("ragetag","token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        //TODO: IF POSSIBLE MAKE NOTIFICATION ABOUT WATER CONSUMPTION
        Log.d("ragetag","new notif: ${p0.data}")
    }
}