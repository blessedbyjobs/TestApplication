package com.android.blessed.testapplication.network

import com.android.blessed.testapplication.db.FilmDAO
import com.android.blessed.testapplication.db.SimpleFilm
import io.reactivex.Observable
import com.android.blessed.testapplication.models.DiscoverResponse
import io.reactivex.Single
import javax.inject.Inject

class FilmsRepository @Inject constructor(val api: ServerAPI, val dao: FilmDAO) {
    fun discoverFilms() : Observable<DiscoverResponse> {
        return api.discoverFilms(
            StringUtils.API_KEY_VALUE,
            StringUtils.LANGUAGE_VALUE,
            StringUtils.SORT_BY_VALUE,
            StringUtils.INCLUDE_ADULT_VALUE,
            StringUtils.INCLUDE_VIDEO_VALUE
        )
    }

    fun searchFilms(query: String): Observable<DiscoverResponse> {
        return api.searchFilms(
            StringUtils.API_KEY_VALUE,
            StringUtils.LANGUAGE_VALUE,
            query,
            StringUtils.INCLUDE_ADULT_VALUE
        )
    }

    fun findFilmById(id: Int): Single<List<SimpleFilm>> = dao.findById(id)

    fun insertFilm(film: SimpleFilm) = dao.insert(film)

    fun deleteFilm(film: SimpleFilm) = dao.remove(film)
}