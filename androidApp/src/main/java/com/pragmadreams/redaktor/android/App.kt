package com.pragmadreams.redaktor.android

import android.app.Application
import com.pragmadreams.redaktor.SharedConfig
import com.pragmadreams.redaktor.util.FileDBContentProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var fileDbProvider: FileDBContentProvider

    override fun onCreate() {
        super.onCreate()

        SharedConfig.setup(fileDBContentProvider = fileDbProvider)
    }
}