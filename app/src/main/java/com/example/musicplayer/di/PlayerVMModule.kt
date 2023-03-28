package com.example.musicplayer.di

import com.example.musicplayer.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerVMModule = module {
    viewModel { PlayerViewModel(get(), get()) }
}