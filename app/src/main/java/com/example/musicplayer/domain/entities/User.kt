package com.example.musicplayer.domain.entities

import com.example.musicplayer.data.entities.UserDBEntity

data class User(
    val id: Int,
    val login: String,
    val email: String,
    val password: String
) {
    fun toUserDBEntity() = UserDBEntity(login, email, password)
}