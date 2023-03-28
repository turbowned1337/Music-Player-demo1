package com.example.musicplayer.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.data.entities.TrackDBEntity
import com.example.musicplayer.domain.entities.Track

fun List<TrackDBEntity>.toTrackList(): LiveData<List<Track>> {
    val newList: MutableList<Track> = mutableListOf()
    this.forEach {
        newList.add(
            Track(
                it.id,
                it.coverURL,
                it.previewURL,
                it.name,
                it.performer,
                it.duration,
                it.isFavorite
            )
        )
    }
    return MutableLiveData<List<Track>>(newList)
}