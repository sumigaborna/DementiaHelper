package hr.ferit.sumigaborna.dementiahelper.memory_game.di

import hr.ferit.sumigaborna.dementiahelper.memory_game.MemoryGameVM
import hr.ferit.sumigaborna.dementiahelper.memory_game.ui.MemoryGameListController
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val memoryGameModule = module {
    viewModel {
        MemoryGameVM(
            get(),
            get()
        )
    }

    factory { MemoryGameListController() }
}