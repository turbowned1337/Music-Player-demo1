package com.example.musicplayer.di

import com.example.musicplayer.utils.MusicPlayer
import org.koin.dsl.module

val musicPlayerModule = module {
    single { MusicPlayer() }
}