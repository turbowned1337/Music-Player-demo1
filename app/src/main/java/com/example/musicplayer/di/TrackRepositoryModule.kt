package com.example.musicplayer.di

import com.example.musicplayer.data.db.TrackDBEntityRepository
import com.example.musicplayer.domain.interfaces.ITrackDBEntityRepository
import org.koin.dsl.module

val trackRepositoryModule = module {
    single<ITrackDBEntityRepository> { TrackDBEntityRepository(get()) }
}