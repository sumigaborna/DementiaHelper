package hr.ferit.sumigaborna.dementiahelper.app.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.common.ALARM_NOTE
import hr.ferit.sumigaborna.dementiahelper.app.common.CHANNEL_ID
import hr.ferit.sumigaborna.dementiahelper.app.common.KEY_ALARM
import hr.ferit.sumigaborna.dementiahelper.app.common.KEY_NOTIFICATION_STOP_MUSIC_PLAYER
import hr.ferit.sumigaborna.dementiahelper.app.ui.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

class AlarmReceiver : BroadcastReceiver(),KoinComponent{

    private val mediaPlayer by inject<MediaPlayer>()
    private val vibrator by inject<Vibrator>()

    override fun onReceive(context: Context, intent: Intent) {
        val intentMainActivity = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_NOTIFICATION_STOP_MUSIC_PLAYER,KEY_ALARM)
        }
        val pendingIntentMainActivity = PendingIntent.getActivity(context,0,intentMainActivity,PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilderMap = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Dementia App Alarm")
            .setContentText("Note: "+(intent.extras?.getString(ALARM_NOTE)?.capitalize() ?: " You have an alarm set!")+ " Tap to turn off" )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntentMainActivity)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)){notify(2,notificationBuilderMap.build())}

        mediaPlayer.isLooping = true
        mediaPlayer.start()
        vibrator.vibrate(VibrationEffect.createOneShot(120*1000, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}