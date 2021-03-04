package com.example.androidkotlinfundamentals.lessons4

import android.app.Application
import timber.log.Timber

class ClickerApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}