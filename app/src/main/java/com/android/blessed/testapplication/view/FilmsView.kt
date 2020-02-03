package com.android.blessed.testapplication.view

import com.android.blessed.testapplication.data.Film
import com.arellomobile.mvp.MvpView

interface FilmsView : MvpView {
    fun displayDiscoveredFilms(films: List<Film>)

    fun showProgressBar()
    fun hideProgressBar()

    fun showHorizontalProgressBar()
    fun hideHorizontalProgressBar()

    fun showRequestError()
    fun showLoadingError()
    fun hideLoadingError()
    fun showEmptyResultsError(query: String)
    fun hideEmptyResultsError()
}