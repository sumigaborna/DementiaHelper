package hr.ferit.sumigaborna.dementiahelper.reminder.di

import android.app.AlarmManager
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.media.MediaPlayer
import android.os.Vibrator
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.reminder.ui.ReminderFragmentController
import hr.ferit.sumigaborna.dementiahelper.reminder.ui.ReminderFragmentListener
import hr.ferit.sumigaborna.dementiahelper.reminder.view_model.ReminderVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reminderModule = module {
    viewModel {
        ReminderVM(
            get(),
            get(),
            get(),
            get()
        )
    }

    factory { (listener: ReminderFragmentListener)-> ReminderFragmentController(listener) }
    single { (get<Context>().getSystemService(ALARM_SERVICE) as AlarmManager) }
    single { MediaPlayer.create(get(), R.raw.alarm) }
    single { get<Context>().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
}