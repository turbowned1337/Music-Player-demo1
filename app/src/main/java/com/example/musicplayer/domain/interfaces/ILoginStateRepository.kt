package com.example.musicplayer.domain.interfaces

interface ILoginStateRepository {
    fun saveLoginState(newState: Boolean)

    fun getLoginState(): Boolean
}