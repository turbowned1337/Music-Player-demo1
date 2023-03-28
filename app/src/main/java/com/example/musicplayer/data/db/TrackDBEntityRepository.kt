package com.example.musicplayer.data.db

import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.musicplayer.domain.entities.Track
import com.example.musicplayer.domain.interfaces.ITrackDBEntityRepository
import com.example.musicplayer.utils.toTrackList

class TrackDBEntityRepository(private val playerDatabase: PlayerDatabase) :
    ITrackDBEntityRepository {

    override fun getTracks() =
        playerDatabase.playerDBDao().getSongsList().asLiveData()
            .switchMap { it.toTrackList() }

    override fun getFavouriteTracks() =
        playerDatabase.playerDBDao().getFavouriteSongsList().asLiveData()
            .switchMap { it.toTrackList() }

    override fun getTracksByFilter(queryString: String) =
        playerDatabase.playerDBDao().getSearchedSongs(queryString).asLiveData()
            .switchMap { it.toTrackList() }

    override suspend fun addTrack(newTrack: Track) =
        playerDatabase.playerDBDao().insertSong(newTrack.toTrackDBEntity())

    override suspend fun deleteTrack(keyTrack: Track) =
        playerDatabase.playerDBDao().deleteSong(keyTrack.toTrackDBEntity())

    override suspend fun updateTrack(keyTrack: Track) =
        playerDatabase.playerDBDao().updateSong(keyTrack.toTrackDBEntity())
}