package com.grandefirano.rickandmortycharacterfinder.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
@Module
@InstallIn(ApplicationComponent::class)
class CoroutineModule {

    @Provides
    fun provideCoroutineScope():CoroutineScope?{
        return null
    }

}