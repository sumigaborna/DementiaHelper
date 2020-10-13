package hr.ferit.sumigaborna.dementiahelper.memory_lane.di

import com.google.firebase.storage.FirebaseStorage
import hr.ferit.sumigaborna.dementiahelper.memory_lane.ui.MemoryLaneFragmentController
import hr.ferit.sumigaborna.dementiahelper.memory_lane.ui.MemoryLaneFragmentListener
import hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model.MemoryLaneVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val memoryLaneModule = module {
    viewModel {
        MemoryLaneVM(
            get(),
            get()
        )
    }
    single { FirebaseStorage.getInstance() }
    factory { (listener: MemoryLaneFragmentListener)->(MemoryLaneFragmentController(
        listener
    )) }
}