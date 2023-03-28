package com.example.musicplayer.di

import com.example.musicplayer.data.preferences.LoginStateRepository
import com.example.musicplayer.domain.interfaces.ILoginStateRepository
import org.koin.dsl.module

val loginStateModule = module {
    single<ILoginStateRepository> { LoginStateRepository(get()) }
}