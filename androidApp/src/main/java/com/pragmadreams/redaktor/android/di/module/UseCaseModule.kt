package com.pragmadreams.redaktor.android.di.module

import com.pragmadreams.redaktor.domain.usecase.EditorUseCase
import com.pragmadreams.redaktor.factory.FileUseCaseFactory
import com.pragmadreams.redaktor.factory.UseCaseFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    private val factory: UseCaseFactory = FileUseCaseFactory()

    @Provides
    fun provideEditorUseCase() : EditorUseCase {
        return factory.createEditorUseCase()
    }
}