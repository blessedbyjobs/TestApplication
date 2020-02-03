package com.android.blessed.testapplication.view

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.blessed.testapplication.R
import com.android.blessed.testapplication.data.Film
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.film_recycler_view_item.view.*
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import java.text.SimpleDateFormat
import java.util.*

class FilmsController(
    val onClickListener: (data: Film) -> Unit,
    val onFavouriteClickListener: (data: Film, bool: Boolean) -> Unit
) : BindableItemController<Film, FilmsController.Holder>() {
    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Film): String = data.id.hashCode().toString()

    inner class Holder(parent: ViewGroup) : BindableViewHolder<Film>(parent,
        R.layout.film_recycler_view_item
    ) {
        private lateinit var data: Film
        private val filmTitle: TextView
        private val filmDescription: TextView
        private val filmPoster: ImageView
        private val filmReleaseDate: TextView
        private val filmLike: ImageView

        private var isFavourite = false

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

            itemView.film_title.viewTreeObserver.addOnGlobalLayoutListener {
                filmDescription.maxLines = if (itemView.film_title.height > 90) 3 else 4
            }
            filmDescription.text = data.overview

            isFavourite = data.isFavourite
            filmLike.background = this.filmLike.context.getDrawable(R.drawable.ic_heart.takeIf { !isFavourite }
                ?: R.drawable.ic_heart_fill)

            Glide.with(this.itemView)
                .asDrawable()
                .load(if (data.safePosterPath != "") data.safePosterPath else R.drawable.placeholder)
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
                onClickListener(data)
            }

            filmLike.setOnClickListener {
                onFavouriteClickListener(data, isFavourite)
                isFavourite = !isFavourite
                filmLike.background = this.filmLike.context.getDrawable(R.drawable.ic_heart.takeIf { !isFavourite }
                    ?: R.drawable.ic_heart_fill)
            }
        }

        private fun formatDate(date: String): String {
            val generalDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            return try {
                val dateObj = generalDate.parse(date) as Date

                val normalDate = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.getDefault())
                normalDate.format(dateObj).toString().dropLast(2)
            } catch (e: Exception) {
                "Неизвестно"
            }
        }
    }
}