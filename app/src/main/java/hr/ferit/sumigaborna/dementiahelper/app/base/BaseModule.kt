package hr.ferit.sumigaborna.dementiahelper.app.base

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val baseModule = module {
    single { androidApplication().resources }
}