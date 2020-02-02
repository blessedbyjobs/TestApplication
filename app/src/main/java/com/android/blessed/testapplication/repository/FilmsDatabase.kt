package com.android.blessed.testapplication.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.blessed.testapplication.data.FilmsDAO
import com.android.blessed.testapplication.data.SimpleFilm

@Database(entities = [SimpleFilm::class], version = 1)
abstract class FilmsDatabase : RoomDatabase() {
    abstract fun getFilmDAO(): FilmsDAO
}