package com.android.blessed.testapplication.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.android.blessed.testapplication.models.DiscoverResponse

interface ServerAPI {
    @GET(StringUtils.DISCOVER)
    fun discoverFilms(
        @Query(StringUtils.API_KEY) API_KEY : String,
        @Query(StringUtils.LANGUAGE) LANGUAGE : String,
        @Query(StringUtils.SORT_BY) SORT_BY : String,
        @Query(StringUtils.INCLUDE_ADULT) INCLUDE_ADULT : String,
        @Query(StringUtils.INCLUDE_VIDEO) INCLUDE_VIDEO : String
    ) : Observable<DiscoverResponse>

    companion object Factory {
        fun create(): ServerAPI {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(StringUtils.BASE_URL)
                .build()

            return retrofit.create(ServerAPI::class.java)
        }
    }
}