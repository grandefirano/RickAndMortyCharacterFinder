package com.grandefirano.rickandmortycharacterfinder

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("character")
    fun getListOfCharacters():Deferred<NetworkCharactersContainer>

    @GET("character/{id}")
    fun getCharacter(@Path("id")id:Int):Deferred<NetworkCharacter>
}

