package com.android.blessed.testapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film_table")
class SimpleFilm(@PrimaryKey @ColumnInfo(name = "id") val id: Int)