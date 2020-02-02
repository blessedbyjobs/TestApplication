package com.android.blessed.testapplication.app

import android.app.Application
import com.android.blessed.testapplication.MainPresenter
import com.android.blessed.testapplication.db.RoomModule
import com.android.blessed.testapplication.db.FilmDAO
import com.android.blessed.testapplication.db.FilmDatabase
import com.android.blessed.testapplication.network.FilmsRepository
import com.android.blessed.testapplication.network.NetworkModule
import com.android.blessed.testapplication.network.ServerAPI
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [AppModule::class, RoomModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)

    fun filmDAO(): FilmDAO

    fun filmDatabase(): FilmDatabase

    fun filmsRepository(): FilmsRepository

    fun serverAPI(): ServerAPI

    fun application(): Application
}