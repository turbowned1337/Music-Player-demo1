package com.example.musicplayer.data.remote

import com.example.musicplayer.data.entities.SpotifySongResponseModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyAPIService {
    @GET("search")
    suspend fun getTrackByQuery(
        @Query("q") queryString: String,
        @Query("type") type: String,
        @Query("limit") limit: String,
        @Header("Authorization") token: String
    ): SpotifySongResponseModel
}