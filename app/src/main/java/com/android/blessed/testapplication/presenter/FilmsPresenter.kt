package com.android.blessed.testapplication.presenter

import android.app.Application
import com.android.blessed.testapplication.di.AppModule
import com.android.blessed.testapplication.di.DaggerAppComponent
import com.android.blessed.testapplication.di.RoomModule
import com.android.blessed.testapplication.data.SimpleFilm
import com.android.blessed.testapplication.data.Film
import com.android.blessed.testapplication.repository.FilmsRepository
import com.android.blessed.testapplication.view.FilmsView
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

@InjectViewState
class FilmsPresenter(application: Application) : MvpPresenter<FilmsView>(),
    FilmsPresenterMVP {
    @Inject
    lateinit var repository: FilmsRepository

    var films = mutableListOf<Film>()

    init {
        DaggerAppComponent.builder()
            .appModule(AppModule(application))
            .roomModule(RoomModule(application))
            .build()
            .inject(this)

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
            .flatMapIterable { films -> films.results }
            .flatMap { film ->
                repository.findFilmById(film.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map { results -> film.copy(isFavourite = results.isNotEmpty()) }
            }
            .toList()
            .subscribe({ result ->
                films = result
                viewState.displayDiscoveredFilms(films)
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
            .flatMapIterable { films -> films.results }
            .flatMap { film ->
                repository.findFilmById(film.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map { results -> film.copy(isFavourite = results.isNotEmpty()) }
            }
            .toList()
            .subscribe(
                { result ->
                    if (result.isNotEmpty()) {
                        films = result as MutableList<Film>
                        viewState.displayDiscoveredFilms(films)
                    } else {
                        viewState.showEmptyResultsError(title)
                        viewState.displayDiscoveredFilms(ArrayList())
                    }
                }, {
                    if (it is IOException) {
                        viewState.showRequestError()
                    } else {
                        viewState.displayDiscoveredFilms(ArrayList())
                        viewState.showLoadingError()
                    }
                })
    }

    override fun roomRequest(film: Film, isFavourite: Boolean) {
        val simpleFilm = SimpleFilm(film.id)
        if (isFavourite) {
            val completable = Completable.fromAction {
                repository.deleteFilm(simpleFilm)
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { films.find { it.id == film.id }?.isFavourite = !isFavourite }
        } else {
            val completable = Completable.fromAction {
                repository.insertFilm(simpleFilm)
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { films.find { it.id == film.id }?.isFavourite = !isFavourite }
        }
    }
}