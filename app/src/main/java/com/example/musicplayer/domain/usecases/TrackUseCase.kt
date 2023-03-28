package com.example.musicplayer.domain.usecases

import com.example.musicplayer.domain.entities.Track
import com.example.musicplayer.domain.interfaces.ISpotifyAPIRepository
import com.example.musicplayer.domain.interfaces.ITrackDBEntityRepository

class TrackUseCase(
    private val trackDBEntityRepository: ITrackDBEntityRepository,
    private val spotifyAPIRepository: ISpotifyAPIRepository
) {
    fun getAllTracks() = trackDBEntityRepository.getTracks()

    fun getFavouriteTracks() = trackDBEntityRepository.getFavouriteTracks()

    fun getTracksByFilter(queryString: String) =
        trackDBEntityRepository.getTracksByFilter(queryString)

    suspend fun addTrackFromAPI(query: String) {
        val newTrack = spotifyAPIRepository.getTrackByQuery(query)
        if (newTrack != null) {
            trackDBEntityRepository.addTrack(newTrack)
        }
    }

    suspend fun addTrack(track: Track) = trackDBEntityRepository.addTrack(track)

    suspend fun deleteTrack(track: Track) = trackDBEntityRepository.deleteTrack(track)

    suspend fun updateTrack(track: Track) = trackDBEntityRepository.updateTrack(track)

    fun provideToken(token: String) = spotifyAPIRepository.setToken(token)
}