package com.example.musicplayer.di

import com.example.musicplayer.domain.usecases.TrackUseCase
import org.koin.dsl.module

val trackUseCaseModule = module {
    single { TrackUseCase(get(), get()) }
}