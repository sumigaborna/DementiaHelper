package hr.ferit.sumigaborna.dementiahelper.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.baseModule
import hr.ferit.sumigaborna.dementiahelper.app.common.CHANNEL_ID
import hr.ferit.sumigaborna.dementiahelper.authentication.di.authModule
import hr.ferit.sumigaborna.dementiahelper.app.di.networkingModule
import hr.ferit.sumigaborna.dementiahelper.carer.di.carerModule
import hr.ferit.sumigaborna.dementiahelper.home.di.homeModule
import hr.ferit.sumigaborna.dementiahelper.memory_game.di.memoryGameModule
import hr.ferit.sumigaborna.dementiahelper.memory_lane.di.memoryLaneModule
import hr.ferit.sumigaborna.dementiahelper.notes.di.notesModule
import hr.ferit.sumigaborna.dementiahelper.patient.di.patientModule
import hr.ferit.sumigaborna.dementiahelper.profile.di.profileModule
import hr.ferit.sumigaborna.dementiahelper.reaction_game.di.reactionGameModule
import hr.ferit.sumigaborna.dementiahelper.reminder.di.reminderModule
import hr.ferit.sumigaborna.dementiahelper.todo.di.todoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DementiaHelperApp : Application(){
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startKoin {
            androidContext(this@DementiaHelperApp)
            modules(
                listOf(
                    baseModule,
                    authModule,
                    networkingModule,
                    homeModule,
                    profileModule,
                    carerModule,
                    patientModule,
                    notesModule,
                    todoModule,
                    reminderModule,
                    memoryLaneModule,
                    reactionGameModule,
                    memoryGameModule
                )
            )
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel)
            val descriptionText = getString(R.string.notifications)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}