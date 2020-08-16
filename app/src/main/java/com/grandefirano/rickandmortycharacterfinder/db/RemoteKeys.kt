package com.grandefirano.rickandmortycharacterfinder.db

import androidx.room.*

@Dao
interface RemoteKeysDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey:List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE characterId=:characterId")
    suspend fun remoteKeysCharacterId(characterId: Int):RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
@Entity(tableName = "remote_keys")
data class RemoteKeys (
    @PrimaryKey(autoGenerate = false)
    val characterId:Int,
    val prevKey:Int?,
    val nextKey:Int?
)

