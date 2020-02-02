package com.android.blessed.testapplication

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blessed.testapplication.models.Film
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import java.util.concurrent.TimeUnit

class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun provideMainPresenter() = MainPresenter(application)

    private lateinit var easyFilmAdapter: EasyAdapter
    private lateinit var filmController: FilmController

    private var scrollPosition: Int = -1
    private var queryString: String = ""
    private var orientationChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSearchBar()
        setupRecyclerView()

        swipe_to_refresh_films.setOnRefreshListener {
            if (queryString == "") {
                mainPresenter.discoverFilms()
            } else {
                mainPresenter.searchFilms(queryString)
            }
            hideLoadingError()
            hideEmptyResultsError()
            swipe_to_refresh_films.isRefreshing = false
        }

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("SCROLL_STATE")
            orientationChanged = savedInstanceState.getBoolean("ORIENTATION")
        }
    }

    override fun onPause() {
        super.onPause()
        scrollPosition =
            (films_recycler_view.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        orientationChanged = true

        outState.putInt("SCROLL_STATE", scrollPosition)
        outState.putBoolean("ORIENTATION", orientationChanged)
    }

    private fun setupSearchBar() {
        val clearQueryButton: ImageView? =
            search_view.findViewById(resources.getIdentifier("android:id/search_close_btn", null, null))
        clearQueryButton?.isEnabled = false
        clearQueryButton?.setImageDrawable(null)

        val disposable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    subscriber.onNext(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    subscriber.onNext(query)
                    return false
                }
            })
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                run {
                    if (!orientationChanged) {
                        queryString = text
                        if (queryString == "") {
                            mainPresenter.discoverFilms()
                        } else {
                            mainPresenter.searchFilms(queryString)
                        }
                    }
                    orientationChanged = false
                }
            }
    }

    private fun setupRecyclerView() {
        films_recycler_view.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        films_recycler_view.layoutManager = layoutManager

        easyFilmAdapter = EasyAdapter()
        films_recycler_view.adapter = easyFilmAdapter

        filmController = FilmController(
            onClickListener = {
                CustomSnackBar(Snackbar.make(main_container, it.title, Snackbar.LENGTH_LONG), this).snackbar.show()
            },
            onFavouriteClickListener = {
                    film: Film, isFavourite: Boolean -> mainPresenter.roomRequest(film, isFavourite)
            })
    }

    override fun displayDiscoveredFilms(films: List<Film>) {
        val itemList = ItemList.create().addAll(films, filmController)
        easyFilmAdapter.setItems(itemList)

        if (scrollPosition != -1) {
            val state: Int = scrollPosition
            Handler().postDelayed({ films_recycler_view.scrollToPosition(state) }, 200)
            scrollPosition = -1
        }
    }

    override fun showProgressBar() {
        progress_bar_container.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar_container.visibility = View.INVISIBLE
    }

    override fun showHorizontalProgressBar() {
        horizontal_progress_bar.visibility = View.VISIBLE
    }

    override fun hideHorizontalProgressBar() {
        horizontal_progress_bar.visibility = View.INVISIBLE
    }

    override fun showRequestError() =
        CustomSnackBar(
            Snackbar.make(
                findViewById(R.id.films_recycler_view), resources.getString(R.string.query_error),
                Snackbar.LENGTH_LONG
            ), this
        ).snackbar.show()

    override fun showLoadingError() {
        error_container.visibility = View.VISIBLE
    }

    override fun hideLoadingError() {
        error_container.visibility = View.INVISIBLE
    }

    override fun showEmptyResultsError(query: String) {
        empty_results_textview.text = resources.getString(R.string.empty_results_error, query)
        empty_results_container.visibility = View.VISIBLE
    }

    override fun hideEmptyResultsError() {
        empty_results_container.visibility = View.INVISIBLE
    }
}
