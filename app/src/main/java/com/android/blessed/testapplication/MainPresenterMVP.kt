package com.android.blessed.testapplication

import com.android.blessed.testapplication.models.Film

interface MainPresenterMVP {
    fun discoverFilms()
    fun searchFilms(title: String)
    fun roomRequest(film: Film, isFavourite: Boolean)
}