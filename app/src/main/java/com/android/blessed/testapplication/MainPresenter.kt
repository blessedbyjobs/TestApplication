package com.android.blessed.testapplication

import com.android.blessed.testapplication.models.Film
import com.android.blessed.testapplication.network.FilmsRepositoryProvider
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), MainPresenterMVP {
    private val repository = FilmsRepositoryProvider.provideFilmsRepository()

    var films: List<Film> = ArrayList()

    init {
        discoverFilms()
    }

    override fun discoverFilms() {
        viewState.hideLoadingError()
        viewState.hideEmptyResultsError()

        if (films.isEmpty()) {
            viewState.showProgressBar()
        } else {
            viewState.showHorizontalProgressBar()
        }

        val disposable = repository.discoverFilms()
            .doOnTerminate {
                viewState.hideHorizontalProgressBar()
                viewState.hideProgressBar()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    run {
                        films = result.results
                        viewState.displayDiscoveredFilms(films)
                    }
                }, {
                    if (films.isEmpty()) {
                        viewState.showLoadingError()
                    } else {
                        viewState.showRequestError()
                    }
                })
    }

    override fun searchFilms(title: String) {
        viewState.hideLoadingError()
        viewState.hideEmptyResultsError()

        if (films.isEmpty()) {
            viewState.showProgressBar()
        } else {
            viewState.showHorizontalProgressBar()
        }

        val disposable = repository.searchFilms(title)
            .doOnTerminate {
                viewState.hideHorizontalProgressBar()
                viewState.hideProgressBar()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    run {
                        if (result.results.isNotEmpty()) {
                            films = result.results
                            viewState.displayDiscoveredFilms(films)
                        } else {
                            viewState.showEmptyResultsError(title)
                            viewState.displayDiscoveredFilms(ArrayList())
                        }
                    }
                }, {
                    if (it is IOException) {
                        viewState.showRequestError()
                    } else {
                        viewState.showLoadingError()
                    }
                })
    }
}