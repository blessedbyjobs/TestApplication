package com.android.blessed.testapplication.app

import android.app.Application
import dagger.Module
import javax.inject.Singleton
import dagger.Provides

@Module
class AppModule(private var application: Application) {
    @Provides
    @Singleton
    internal fun providesApplication(): Application {
        return application
    }
}