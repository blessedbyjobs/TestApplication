package com.android.blessed.testapplication.network

import io.reactivex.Observable
import com.android.blessed.testapplication.models.DiscoverResponse

object FilmsRepositoryProvider {
    fun provideFilmsRepository(): FilmsRepository {
        return FilmsRepository(ServerAPI.create())
    }
}

class FilmsRepository(private val api: ServerAPI) {
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
}