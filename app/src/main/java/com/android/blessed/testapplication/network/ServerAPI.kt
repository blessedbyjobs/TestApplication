package com.android.blessed.testapplication.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import com.android.blessed.testapplication.data.ServerResponse
import com.android.blessed.testapplication.utils.StringUtils

interface ServerAPI {
    @GET(StringUtils.DISCOVER)
    fun discoverFilms(
        @Query(StringUtils.API_KEY) API_KEY : String,
        @Query(StringUtils.LANGUAGE) LANGUAGE : String,
        @Query(StringUtils.SORT_BY) SORT_BY : String,
        @Query(StringUtils.INCLUDE_ADULT) INCLUDE_ADULT : String,
        @Query(StringUtils.INCLUDE_VIDEO) INCLUDE_VIDEO : String
    ) : Observable<ServerResponse>

    @GET(StringUtils.SEARCH)
    fun searchFilms(
        @Query(StringUtils.API_KEY) API_KEY : String,
        @Query(StringUtils.LANGUAGE) LANGUAGE : String,
        @Query(StringUtils.SEARCH_QUERY) QUERY : String,
        @Query(StringUtils.INCLUDE_ADULT) INCLUDE_ADULT : String
    ) : Observable<ServerResponse>
}