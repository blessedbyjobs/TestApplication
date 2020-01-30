package com.android.blessed.testapplication

import com.android.blessed.testapplication.models.Film
import com.android.blessed.testapplication.network.FilmsRepositoryProvider
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), MainPresenterMVP {
    var films: List<Film> = ArrayList()

    init {
        discoverFilms()
    }

    override fun discoverFilms() {
        if (films.isEmpty()) {
            viewState.hideLoadingError()
            viewState.showProgressBar()
        }
        else {
            viewState.showHorizontalProgressBar()
        }

        val repository = FilmsRepositoryProvider.provideFilmsRepository()
        val disposable = repository.discoverFilms()
            .doOnTerminate {
                viewState.hideHorizontalProgressBar()
                viewState.hideProgressBar()
                viewState.hideLoadingError()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe (
                { result ->
                    run {
                        films = result.results
                        viewState.displayDiscoveredFilms(result.results)
                    }
                }, {
                    if (films.isEmpty()) {
                        viewState.showLoadingError()
                    }
                    else {
                        viewState.showRequestError()
                    }
                })
    }

}