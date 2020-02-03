package com.android.blessed.testapplication.presenter

import com.android.blessed.testapplication.data.Film

interface FilmsPresenterMVP {
    fun discoverFilms()
    fun searchFilms(title: String)
    fun roomRequest(film: Film, isFavourite: Boolean)
}