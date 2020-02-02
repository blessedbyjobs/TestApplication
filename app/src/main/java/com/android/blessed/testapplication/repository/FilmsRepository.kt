package com.android.blessed.testapplication.repository

import com.android.blessed.testapplication.data.FilmsDAO
import com.android.blessed.testapplication.data.SimpleFilm
import io.reactivex.Observable
import com.android.blessed.testapplication.data.ServerResponse
import com.android.blessed.testapplication.network.ServerAPI
import com.android.blessed.testapplication.utils.StringUtils
import io.reactivex.Single
import javax.inject.Inject

class FilmsRepository @Inject constructor(val api: ServerAPI, val dao: FilmsDAO) {
    fun discoverFilms() : Observable<ServerResponse> {
        return api.discoverFilms(
            StringUtils.API_KEY_VALUE,
            StringUtils.LANGUAGE_VALUE,
            StringUtils.SORT_BY_VALUE,
            StringUtils.INCLUDE_ADULT_VALUE,
            StringUtils.INCLUDE_VIDEO_VALUE
        )
    }

    fun searchFilms(query: String): Observable<ServerResponse> {
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