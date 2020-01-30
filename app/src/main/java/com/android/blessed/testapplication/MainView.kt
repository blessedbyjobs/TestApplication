package com.android.blessed.testapplication

import com.android.blessed.testapplication.models.Film
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*

@StateStrategyType(AddToEndSingleTagStrategy::class)
interface MainView : MvpView {
    fun displayDiscoveredFilms(films: List<Film>)

    fun showProgressBar()
    fun hideProgressBar()

    fun showHorizontalProgressBar()
    fun hideHorizontalProgressBar()

    fun showRequestError()
    fun showLoadingError()
    fun hideLoadingError()
}