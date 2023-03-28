package com.example.musicplayer.domain.entities

import com.example.musicplayer.data.entities.TrackDBEntity

data class Track(
    val id: Int,
    val coverURL: String,
    val previewURL: String,
    val name: String,
    val performer: String,
    val duration: Int,
    var isFavorite: Boolean
) {
    fun toTrackDBEntity() =
        TrackDBEntity(id, coverURL, previewURL, name, performer, duration, isFavorite)
}