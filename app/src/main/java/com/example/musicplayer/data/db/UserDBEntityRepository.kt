package com.example.musicplayer.data.db

import com.example.musicplayer.domain.entities.User
import com.example.musicplayer.domain.interfaces.IUserDBEntityRepository

class UserDBEntityRepository(private val database: PlayerDatabase) : IUserDBEntityRepository {
    override suspend fun addUser(user: User) =
        database.playerDBDao().insertUser(user.toUserDBEntity())

    override suspend fun getUser(login: String) =
        database.playerDBDao().getUserByLogin(login)?.toUser()
}
