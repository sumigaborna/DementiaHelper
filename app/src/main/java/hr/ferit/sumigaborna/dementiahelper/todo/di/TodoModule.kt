package hr.ferit.sumigaborna.dementiahelper.todo.di

import hr.ferit.sumigaborna.dementiahelper.todo.ui.TodoFragmentController
import hr.ferit.sumigaborna.dementiahelper.todo.ui.TodoFragmentListener
import hr.ferit.sumigaborna.dementiahelper.todo.view_model.TodoVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val todoModule = module {
    viewModel { TodoVM(get(),get()) }
    factory { (listener: TodoFragmentListener)->(TodoFragmentController(listener)) }
}