package com.example.musicplayer.data.db

import androidx.room.*
import com.example.musicplayer.data.entities.TrackDBEntity
import com.example.musicplayer.data.entities.UserDBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDBDao {
    @Query("SELECT * FROM TrackDBEntity")
    fun getSongsList(): Flow<List<TrackDBEntity>>

    @Query("SELECT * FROM TrackDBEntity WHERE isFavorite == 1")
    fun getFavouriteSongsList(): Flow<List<TrackDBEntity>>

    @Query("SELECT * FROM TrackDBEntity WHERE name LIKE '%' || :name || '%' OR performer LIKE '%' || :name || '%'")
    fun getSearchedSongs(name: String): Flow<List<TrackDBEntity>>

    @Query("SELECT * FROM UserDBEntity WHERE login == (:login)")
    suspend fun getUserByLogin(login: String): UserDBEntity?

    @Insert
    suspend fun insertUser(userDBEntityData: UserDBEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(trackDBEntity: TrackDBEntity)

    @Insert
    suspend fun insertSongs(trackDBEntities: List<TrackDBEntity>)

    @Delete
    suspend fun deleteSong(trackDBEntity: TrackDBEntity)

    @Update
    suspend fun updateSong(trackDBEntity: TrackDBEntity)
}