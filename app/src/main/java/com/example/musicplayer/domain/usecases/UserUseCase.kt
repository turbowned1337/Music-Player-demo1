package com.example.musicplayer.domain.usecases

import com.example.musicplayer.domain.entities.User
import com.example.musicplayer.domain.interfaces.ILoginStateRepository
import com.example.musicplayer.domain.interfaces.IUserDBEntityRepository

class UserUseCase(
    private val loginStateRepository: ILoginStateRepository,
    private val userDBEntityRepository: IUserDBEntityRepository
) {
    suspend fun addUser(user: User) {
        userDBEntityRepository.addUser(user)
    }

    suspend fun getUserData(login: String) = userDBEntityRepository.getUser(login)

    fun saveLoginState(newState: Boolean) = loginStateRepository.saveLoginState(newState)

    fun getLoginState() = loginStateRepository.getLoginState()
}