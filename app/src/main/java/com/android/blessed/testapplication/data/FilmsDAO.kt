package com.android.blessed.testapplication.data

import androidx.room.*
import io.reactivex.Single

@Dao
interface FilmsDAO {
    @Query("SELECT * FROM film_table WHERE id=:id")
    fun findById(id: Int): Single<List<SimpleFilm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: SimpleFilm)

    @Delete
    fun remove(film: SimpleFilm)
}