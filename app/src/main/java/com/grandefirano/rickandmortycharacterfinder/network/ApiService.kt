package com.grandefirano.rickandmortycharacterfinder.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("character")
    fun getListOfCharacters():Deferred<NetworkCharactersContainer>

    @GET("character/{id}")
    fun getCharacter(@Path("id")id:Int):Deferred<NetworkCharacter>
}

