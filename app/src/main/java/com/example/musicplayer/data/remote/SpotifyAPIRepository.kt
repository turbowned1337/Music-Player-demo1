package com.example.musicplayer.data.remote

import com.example.musicplayer.domain.entities.Track
import com.example.musicplayer.domain.interfaces.ISpotifyAPIRepository
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SpotifyAPIRepository : ISpotifyAPIRepository {
    private var spotifyToken: String = ""

    private val moshi = Moshi.Builder()
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.spotify.com/v1/")
        .build()

    private val requester = retrofit.create(SpotifyAPIService::class.java)

    override suspend fun getTrackByQuery(queryString: String): Track? {
        return try {
            val foundTracks = requester.getTrackByQuery(
                queryString,
                "track",
                "1",
                "Bearer $spotifyToken"
            ).tracks.trackList
            if (foundTracks.isNotEmpty()) {
                return foundTracks[0].toTrack()
            } else {
                null
            }
        } catch (exception: HttpException) {
            null
        } catch (exception: JsonDataException) {
            null
        }
    }

    override fun setToken(token: String) {
        spotifyToken = token
    }
}