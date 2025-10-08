package di

import MainViewModel
import data.Database
import org.koin.dsl.module

val appModule = module {
    single { Database() }
    single { MainViewModel(get()) }
}