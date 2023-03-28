package com.example.musicplayer.domain.interfaces

import com.example.musicplayer.domain.entities.User

interface IUserDBEntityRepository {
    suspend fun addUser(user: User)

    suspend fun getUser(login: String): User?
}