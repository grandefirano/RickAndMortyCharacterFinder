package com.grandefirano.rickandmortycharacterfinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grandefirano.rickandmortycharacterfinder.data.DomainCharacter

@Database(
    entities = [DomainCharacter::class,RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class CharacterDatabase:RoomDatabase() {

    abstract fun charactersDao():CharacterDao
    abstract fun remoteKeysDao():RemoteKeysDao


}