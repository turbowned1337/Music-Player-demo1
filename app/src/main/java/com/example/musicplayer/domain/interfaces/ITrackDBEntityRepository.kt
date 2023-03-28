package com.example.musicplayer.domain.interfaces

import androidx.lifecycle.LiveData
import com.example.musicplayer.domain.entities.Track

interface ITrackDBEntityRepository {
    fun getTracks(): LiveData<List<Track>>

    fun getFavouriteTracks(): LiveData<List<Track>>

    fun getTracksByFilter(queryString: String): LiveData<List<Track>>

    suspend fun addTrack(newTrack: Track)

    suspend fun deleteTrack(keyTrack: Track)

    suspend fun updateTrack(keyTrack: Track)
}