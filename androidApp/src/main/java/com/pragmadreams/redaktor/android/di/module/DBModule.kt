package com.pragmadreams.redaktor.android.di.module

import android.app.Application
import android.content.Context
import com.pragmadreams.redaktor.android.platform.AndroidFileDBProvider
import com.pragmadreams.redaktor.util.FileDBContentProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

    @Singleton
    @Provides
    fun provideFileDBProvider(
        @ApplicationContext context: Context,
    ): FileDBContentProvider {
        return AndroidFileDBProvider(application = context as Application)
    }
}