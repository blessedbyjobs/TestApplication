package com.android.blessed.testapplication.db

import androidx.room.*
import io.reactivex.Single

@Dao
interface FilmDAO {
    @Query("SELECT * FROM film_table WHERE id=:id")
    fun findById(id: Int): Single<List<SimpleFilm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: SimpleFilm)

    @Delete
    fun remove(film: SimpleFilm)
}