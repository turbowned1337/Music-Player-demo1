package com.example.musicplayer.utils

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData

enum class PlayerState {
    IDLE,
    PREPARED,
    PREPARING,
    PLAYING,
    PAUSED,
    ENDED
}

class MusicPlayer {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    val playerState = MutableLiveData(PlayerState.IDLE)

    init {
        setup()
    }

    fun prepareSong(url: String?) {
        if (playerState.value == PlayerState.IDLE) {
            playerState.postValue(PlayerState.PREPARING)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        }
    }

    fun togglePause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            playerState.postValue(PlayerState.PAUSED)
        } else {
            mediaPlayer.start()
            playerState.postValue(PlayerState.PLAYING)
        }
    }

    fun seekTo(position: Int) = mediaPlayer.seekTo(position)

    fun getCurrentPosition() = mediaPlayer.currentPosition

    fun getFileDuration() = mediaPlayer.duration

    fun resetPlayer() {
        if (playerState.value != PlayerState.IDLE) {
            mediaPlayer.reset()
            playerState.postValue(PlayerState.IDLE)
        }
    }

    fun isPlaying() = mediaPlayer.isPlaying

    private fun setup() {
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.PREPARED)
            mediaPlayer.start()
        }

        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.ENDED)
        }
    }
}