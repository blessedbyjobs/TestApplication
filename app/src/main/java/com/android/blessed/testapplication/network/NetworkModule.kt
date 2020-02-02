package com.android.blessed.testapplication.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun serverAPI(retrofit: Retrofit): ServerAPI = retrofit.create(ServerAPI::class.java)

    @Singleton
    @Provides
    internal fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(StringUtils.BASE_URL)
        .build()
}