package com.grandefirano.rickandmortycharacterfinder.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.grandefirano.rickandmortycharacterfinder.db.CharacterDatabase
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): CharacterDatabase {

        return Room.databaseBuilder(applicationContext,
            CharacterDatabase::class.java, "RickAndMorty.db")
            .build()
    }
}