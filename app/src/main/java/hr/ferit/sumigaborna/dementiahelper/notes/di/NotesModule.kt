package hr.ferit.sumigaborna.dementiahelper.notes.di

import hr.ferit.sumigaborna.dementiahelper.notes.list.ui.NotesListFragmentController
import hr.ferit.sumigaborna.dementiahelper.notes.list.ui.NotesListListener
import hr.ferit.sumigaborna.dementiahelper.notes.single.ui.NotesSingleFragmentController
import hr.ferit.sumigaborna.dementiahelper.notes.single.ui.NotesSingleListener
import hr.ferit.sumigaborna.dementiahelper.notes.view_model.NotesVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val notesModule = module {
    viewModel { NotesVM(get(),get()) }
    factory { (listener: NotesListListener)->NotesListFragmentController(listener)}
    factory { (listener:NotesSingleListener)->NotesSingleFragmentController(listener) }
}