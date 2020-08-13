package com.grandefirano.rickandmortycharacterfinder.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val GET_LIST_OF_CHARACTERS: String = "character"
const val GET_CHARACTER:String="character/{id}"

interface ApiService {
    @GET(GET_LIST_OF_CHARACTERS)
    suspend fun getListOfCharacters(
        @Query("page") page:Int=1
    ):NetworkCharactersContainer

    @GET(GET_CHARACTER)
    suspend fun getCharacter(@Path("id")id:Int):NetworkCharacter
}

