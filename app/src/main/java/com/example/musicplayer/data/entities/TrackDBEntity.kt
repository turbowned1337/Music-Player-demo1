package com.example.musicplayer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicplayer.domain.entities.Track

@Entity
data class TrackDBEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "coverPath")
    val coverURL: String,
    @ColumnInfo(name = "previewURL")
    val previewURL: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "performer")
    val performer: String,
    @ColumnInfo(name = "duration")
    val duration: Int,
    @Transient
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
) {
    fun toTrack() = Track(id, coverURL, previewURL, name, performer, duration, isFavorite)
}
