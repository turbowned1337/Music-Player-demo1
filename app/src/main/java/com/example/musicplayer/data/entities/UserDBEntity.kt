package com.example.musicplayer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicplayer.domain.entities.User

@Entity
data class UserDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String
) {
    constructor(login: String, email: String, password: String) : this(0, login, email, password)

    fun toUser() = User(id, login, email, password)
}
