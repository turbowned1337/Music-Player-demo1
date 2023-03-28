package com.example.musicplayer.domain.interfaces

import com.example.musicplayer.domain.entities.Track

interface ISpotifyAPIRepository {
    suspend fun getTrackByQuery(queryString: String): Track?

    fun setToken(token: String)
}