package com.android.blessed.testapplication

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.blessed.testapplication.models.Film
import com.android.blessed.testapplication.network.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import java.text.SimpleDateFormat
import java.util.*

class FilmController(val onClickListener: (data: Film) -> Unit) : BindableItemController<Film, FilmController.Holder>() {
    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Film): String = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Film>(parent, R.layout.film_recycler_view_item) {
        private lateinit var data : Film
        private val filmTitle: TextView
        private val filmDescription: TextView
        private val filmPoster: ImageView
        private val filmReleaseDate: TextView
        private val filmLike: ImageView

        init {
            filmTitle = itemView.findViewById(R.id.film_title)
            filmDescription = itemView.findViewById(R.id.film_description)
            filmPoster = itemView.findViewById(R.id.film_image_view)
            filmReleaseDate = itemView.findViewById(R.id.film_release_date)
            filmLike = itemView.findViewById(R.id.save_to_favourite)
        }

        override fun bind(data: Film) {
            this.data = data
            filmTitle.text = data.title
            filmDescription.text = data.overview

            Glide.with(this.itemView)
                .asDrawable()
                .load(StringUtils.IMAGE_BASE_URL + data.poster_path)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(360, 360)
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        filmPoster.setImageDrawable(resource)
                    }

                })

            filmReleaseDate.text = formatDate(data.release_date)

            this.itemView.setOnClickListener {
                CustomSnackBar(Snackbar.make(it, filmTitle.text, Snackbar.LENGTH_LONG), this.itemView.context).snackbar.show()
            }

            filmLike.setOnClickListener {
                filmLike.background = it.resources.getDrawable(R.drawable.ic_heart_fill)
            }
        }

        private fun formatDate(date: String): String {
            val generalDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateObj = generalDate.parse(date) as Date

            val normalDate = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.getDefault())
            return normalDate.format(dateObj).toString()
        }
    }
}