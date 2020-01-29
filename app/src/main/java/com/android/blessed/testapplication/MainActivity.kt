package com.android.blessed.testapplication

import android.os.Bundle
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clearQueryButton : ImageView? = search_view.findViewById(resources.getIdentifier("android:id/search_close_btn", null, null))
        clearQueryButton?.isEnabled = false
        clearQueryButton?.setImageDrawable(null)

        swipe_to_refresh_films.setOnRefreshListener {
            swipe_to_refresh_films.isRefreshing = false
        }
    }
}
