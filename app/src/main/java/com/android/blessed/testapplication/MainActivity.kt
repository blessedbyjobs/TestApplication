package com.android.blessed.testapplication

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.blessed.testapplication.models.Film
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private lateinit var easyFilmAdapter : EasyAdapter
    private lateinit var filmController: FilmController

    private var scrollPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSearchBar()
        setupRecyclerView()

        swipe_to_refresh_films.setOnRefreshListener {
            mainPresenter.discoverFilms()
        }

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("SCROLL_STATE")
        }
    }

    override fun onPause() {
        super.onPause()
        scrollPosition = (films_recycler_view.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SCROLL_STATE", scrollPosition)
    }

    private fun setupSearchBar() {
        val clearQueryButton : ImageView? = search_view.findViewById(resources.getIdentifier("android:id/search_close_btn", null, null))
        clearQueryButton?.isEnabled = false
        clearQueryButton?.setImageDrawable(null)
    }

    private fun setupRecyclerView() {
        films_recycler_view.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        films_recycler_view.layoutManager = layoutManager

        easyFilmAdapter = EasyAdapter()
        films_recycler_view.adapter = easyFilmAdapter

        filmController = FilmController {}
    }

    override fun displayDiscoveredFilms(films: List<Film>) {
        swipe_to_refresh_films.isRefreshing = false

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
        CustomSnackBar(Snackbar.make(findViewById(R.id.films_recycler_view), resources.getString(R.string.query_error),
            Snackbar.LENGTH_LONG), this).snackbar.show()

    override fun showLoadingError() {
        error_container.visibility = View.VISIBLE
    }

    override fun hideLoadingError() {
        error_container.visibility = View.INVISIBLE
    }
}
