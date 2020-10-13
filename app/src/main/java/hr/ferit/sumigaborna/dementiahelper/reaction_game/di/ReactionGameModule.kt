package hr.ferit.sumigaborna.dementiahelper.reaction_game.di

import hr.ferit.sumigaborna.dementiahelper.reaction_game.list.ReactionGameListController
import hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model.ReactionGameVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val reactionGameModule = module {
    viewModel { ReactionGameVM(get(),get()) }
    factory { ReactionGameListController() }
}