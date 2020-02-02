package com.android.blessed.testapplication.di

import android.app.Application
import com.android.blessed.testapplication.presenter.FilmsPresenter
import com.android.blessed.testapplication.data.FilmsDAO
import com.android.blessed.testapplication.repository.FilmsDatabase
import com.android.blessed.testapplication.repository.FilmsRepository
import com.android.blessed.testapplication.network.ServerAPI
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [AppModule::class, RoomModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(filmsPresenter: FilmsPresenter)

    fun filmsDAO(): FilmsDAO

    fun filmsDatabase(): FilmsDatabase

    fun filmsRepository(): FilmsRepository

    fun serverAPI(): ServerAPI

    fun application(): Application
}