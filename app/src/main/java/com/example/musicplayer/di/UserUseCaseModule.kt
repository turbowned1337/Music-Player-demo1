package com.example.musicplayer.di

import com.example.musicplayer.domain.usecases.UserUseCase
import org.koin.dsl.module

val userUseCaseModule = module {
    single { UserUseCase(get(), get()) }
}