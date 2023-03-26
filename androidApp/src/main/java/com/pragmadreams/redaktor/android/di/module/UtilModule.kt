package com.pragmadreams.redaktor.android.di.module

import com.pragmadreams.redaktor.android.shared.Hub
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {

    @Singleton
    @Provides
    fun provideHub() : Hub {
        return Hub()
    }

}