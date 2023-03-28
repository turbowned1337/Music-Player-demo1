package com.example.musicplayer.di

import com.example.musicplayer.data.remote.SpotifyAPIRepository
import com.example.musicplayer.domain.interfaces.ISpotifyAPIRepository
import org.koin.dsl.module

val spotifyAPIRepositoryModule = module {
    single<ISpotifyAPIRepository> { SpotifyAPIRepository() }
}