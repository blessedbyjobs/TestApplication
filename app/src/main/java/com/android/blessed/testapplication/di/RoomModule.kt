package com.android.blessed.testapplication.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application
import com.android.blessed.testapplication.data.FilmsDAO
import com.android.blessed.testapplication.network.ServerAPI
import com.android.blessed.testapplication.repository.FilmsDatabase
import com.android.blessed.testapplication.repository.FilmsRepository


@Module
class RoomModule(application: Application) {
    private val filmsDatabase: FilmsDatabase = Room.databaseBuilder(application, FilmsDatabase::class.java, "films").build()

    @Singleton
    @Provides
    internal fun providesFilmDatabase(): FilmsDatabase = filmsDatabase

    @Singleton
    @Provides
    internal fun providesFilmDao(demoDatabase: FilmsDatabase): FilmsDAO = demoDatabase.getFilmDAO()

    @Singleton
    @Provides
    internal fun filmsRepository(api: ServerAPI, dao: FilmsDAO): FilmsRepository =
        FilmsRepository(api, dao)

}