package com.android.blessed.testapplication

import com.android.blessed.testapplication.network.FilmsRepositoryProvider
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

@InjectViewState
class MainPresenter : MvpPresenter<MainView>(), MainPresenterMVP {
    override fun discoverFilms() {
        val repository = FilmsRepositoryProvider.provideFilmsRepository()
        repository.discoverFilms()
            .doOnTerminate {}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}