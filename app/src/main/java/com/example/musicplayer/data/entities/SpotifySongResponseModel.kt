package com.example.musicplayer.data.entities

import com.example.musicplayer.domain.entities.Track
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpotifySongResponseModel(
    @Json(name = "tracks")
    val tracks: TrackList
)

@JsonClass(generateAdapter = true)
data class TrackList(
    @Json(name = "items")
    val trackList: List<SpotifyTrack>
)

@JsonClass(generateAdapter = true)
data class SpotifyTrack(
    @Json(name = "album")
    val album: Album,
    @Json(name = "artists")
    val artists: List<Artist>,
    @Json(name = "name")
    val name: String,
    @Json(name = "preview_url")
    val previewURL: String,
) {
    fun toTrack() =
        Track(0, album.images[0].url, previewURL, name, artists[0].name, 29000, false)
}

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "images")
    val images: List<Image>,
)

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "name")
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "url")
    val url: String,
)
