package hr.ferit.sumigaborna.dementiahelper.authentication.di

import com.google.firebase.auth.FirebaseAuth
import hr.ferit.sumigaborna.dementiahelper.authentication.view_model.AuthVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module{
    viewModel {
        AuthVM(get(),get())
    }
    single { FirebaseAuth.getInstance() }
}