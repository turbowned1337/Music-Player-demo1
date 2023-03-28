package com.example.musicplayer.di

import com.example.musicplayer.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginVMModule = module {
    viewModel { LoginViewModel(get()) }
}