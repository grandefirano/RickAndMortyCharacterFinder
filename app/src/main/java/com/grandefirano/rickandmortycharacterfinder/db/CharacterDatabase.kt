package com.grandefirano.rickandmortycharacterfinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Character::class,RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class CharacterDatabase:RoomDatabase() {

    abstract fun charactersDao():CharacterDao
    abstract fun remoteKeysDao():RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getInstance(context: Context): CharacterDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CharacterDatabase::class.java, "RickAndMorty.db")
                .build()
    }
}