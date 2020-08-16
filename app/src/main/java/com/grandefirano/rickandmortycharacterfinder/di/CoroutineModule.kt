package com.grandefirano.rickandmortycharacterfinder.di

import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
@Module
@InstallIn(ApplicationComponent::class)
class CoroutineModule {

    @Provides
    fun providesCoroutineScope():CoroutineScope?{
        return null
    }

}