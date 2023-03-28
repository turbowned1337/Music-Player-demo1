package com.example.musicplayer.presentation

import androidx.lifecycle.*
import com.example.musicplayer.domain.entities.Track
import com.example.musicplayer.domain.usecases.TrackUseCase
import com.example.musicplayer.utils.MusicPlayer
import com.example.musicplayer.utils.PlayerState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackUseCase: TrackUseCase,
    private val musicPlayer: MusicPlayer
) : ViewModel() {
    private val _currentTrackData = MutableLiveData<Track>()
    val currentTrackData: LiveData<Track>
        get() = _currentTrackData

    val playerState: LiveData<PlayerState>
        get() = musicPlayer.playerState

    var currentTrackID = 0

    fun setCurrentTrack(track: Track) {
        viewModelScope.launch {
            musicPlayer.resetPlayer()
            getAllTracks().asFlow().collect {
                val trackIndex = it.indexOf(track)
                _currentTrackData.postValue(it[trackIndex])
                musicPlayer.prepareSong(it[trackIndex].previewURL)
                currentTrackID = trackIndex
                cancel()
            }
        }
    }

    fun nextTrack() {
        viewModelScope.launch {
            musicPlayer.resetPlayer()
            getAllTracks().asFlow().collect {
                if (currentTrackID + 1 >= it.size) {
                    _currentTrackData.postValue(it[0])
                    musicPlayer.prepareSong(it[0].previewURL)
                    currentTrackID = 0
                } else {
                    _currentTrackData.postValue(it[currentTrackID + 1])
                    musicPlayer.prepareSong(it[currentTrackID + 1].previewURL)
                    currentTrackID += 1
                }
                cancel()
            }
        }
    }

    fun previousTrack() {
        viewModelScope.launch {
            musicPlayer.resetPlayer()
            getAllTracks().asFlow().collect {
                if (currentTrackID - 1 < 0) {
                    _currentTrackData.postValue(it.last())
                    musicPlayer.prepareSong(it.last().previewURL)
                    currentTrackID = it.lastIndex
                } else {
                    _currentTrackData.postValue(it[currentTrackID - 1])
                    musicPlayer.prepareSong(it[currentTrackID - 1].previewURL)
                    currentTrackID -= 1
                }
                cancel()
            }
        }
    }

    fun getAllTracks() = trackUseCase.getAllTracks()

    fun getFavouriteTracks() = trackUseCase.getFavouriteTracks()

    fun searchForTracks(queryString: String) = trackUseCase.getTracksByFilter(queryString)

    fun addTrack(track: Track) {
        viewModelScope.launch {
            trackUseCase.addTrack(track)
        }
    }

    fun addSongFromSpotifyAPI(queryString: String) {
        viewModelScope.launch {
            trackUseCase.addTrackFromAPI(queryString)
        }
    }

    fun deleteSong(track: Track) {
        viewModelScope.launch {
            trackUseCase.deleteTrack(track)
        }
    }

    fun updateSong(newTrackInfo: Track) {
        viewModelScope.launch {
            trackUseCase.updateTrack(newTrackInfo)
        }
    }

    fun seekInSong(position: Int) {
        musicPlayer.seekTo(position)
    }

    fun togglePause() {
        if (playerState.value == PlayerState.IDLE) {
            viewModelScope.launch {
                getAllTracks().asFlow().collect {
                    _currentTrackData.postValue(it[0])
                    musicPlayer.prepareSong(it[0].previewURL)
                    cancel()
                }
            }
        } else {
            musicPlayer.togglePause()
        }
    }

    fun getPlayerPosition() = musicPlayer.getCurrentPosition()

    fun getSongDuration() = musicPlayer.getFileDuration()

    fun isTrackPlaying() = musicPlayer.isPlaying()

    fun provideSpotifyToken(token: String) = trackUseCase.provideToken(token)
}