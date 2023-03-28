package com.example.musicplayer

import android.app.Application
import com.example.musicplayer.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlayerApp)
            modules(
                listOf(
                    dataBaseModule,
                    loginStateModule,
                    loginVMModule,
                    userRepositoryModule,
                    playerVMModule,
                    spotifyAPIRepositoryModule,
                    trackRepositoryModule,
                    musicPlayerModule,
                    trackUseCaseModule,
                    userUseCaseModule
                )
            )
        }
    }
}