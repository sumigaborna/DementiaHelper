package hr.ferit.sumigaborna.dementiahelper.carer.di

import android.content.res.Resources
import hr.ferit.sumigaborna.dementiahelper.carer.ui.CarerFragmentController
import hr.ferit.sumigaborna.dementiahelper.carer.ui.CarerFragmentListener
import hr.ferit.sumigaborna.dementiahelper.carer.view_model.CarerVM
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val carerModule = module {
    factory { (listener: CarerFragmentListener)->(CarerFragmentController(listener)) }
    viewModel { CarerVM(get(),get(),get()) }
}