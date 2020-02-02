package com.android.blessed.testapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SimpleFilm::class], version = 1)
abstract class FilmDatabase : RoomDatabase() {
    abstract fun getFilmDAO(): FilmDAO
}