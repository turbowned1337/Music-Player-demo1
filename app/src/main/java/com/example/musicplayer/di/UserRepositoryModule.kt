package com.example.musicplayer.di

import com.example.musicplayer.data.db.UserDBEntityRepository
import com.example.musicplayer.domain.interfaces.IUserDBEntityRepository
import org.koin.dsl.module

val userRepositoryModule = module {
    single<IUserDBEntityRepository> { UserDBEntityRepository(get()) }
}