package hr.ferit.sumigaborna.dementiahelper.profile.di

import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.sumigaborna.dementiahelper.profile.view_model.ProfileVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileVM(get(),get()) }
    single { FirebaseFirestore.getInstance() }
}