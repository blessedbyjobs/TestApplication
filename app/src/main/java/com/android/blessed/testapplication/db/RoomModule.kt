package com.android.blessed.testapplication.db

import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application
import com.android.blessed.testapplication.network.FilmsRepository
import com.android.blessed.testapplication.network.ServerAPI


@Module
class RoomModule(application: Application) {
    private val filmDatabase: FilmDatabase = Room.databaseBuilder(application, FilmDatabase::class.java, "films").build()

    @Singleton
    @Provides
    internal fun providesFilmDatabase(): FilmDatabase = filmDatabase

    @Singleton
    @Provides
    internal fun providesFilmDao(demoDatabase: FilmDatabase): FilmDAO = demoDatabase.getFilmDAO()

    @Singleton
    @Provides
    internal fun filmsRepository(api: ServerAPI, dao: FilmDAO): FilmsRepository = FilmsRepository(api, dao)

}