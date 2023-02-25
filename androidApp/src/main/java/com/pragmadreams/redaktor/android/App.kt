package com.pragmadreams.redaktor.android

import android.app.Application
import com.pragmadreams.redaktor.SharedConfig
import com.pragmadreams.redaktor.android.platform.AndroidFileDBProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        SharedConfig.setup(fileDBContentProvider = AndroidFileDBProvider())
    }
}