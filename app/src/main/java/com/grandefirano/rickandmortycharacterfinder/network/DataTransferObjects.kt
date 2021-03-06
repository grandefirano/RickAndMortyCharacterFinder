package com.grandefirano.rickandmortycharacterfinder.network

import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class NetworkCharactersContainer(val results:List<NetworkCharacter>)

@JsonClass(generateAdapter = true)
data class NetworkCharacter(
    val id:Int,
    val name:String,
    val status:String,
    val species:String,
    val type:String,
    val gender:String,
    @Json(name="origin")
    val originLocation: Location,
    @Json(name="location")
    val presentLocation: Location,
    @Json(name="image")
    val imageUrl:String
)

suspend fun NetworkCharactersContainer.asDomainModel():List<Character>{
    return withContext(Dispatchers.Default) {
        results.map {
            it.asDomainModel()
        }
    }
}

suspend fun NetworkCharacter.asDomainModel(): Character {
    return withContext(Dispatchers.Default) {
        Character(
            id = id,
            name = name,
            status = status,
            species = species,
            gender = gender,
            presentLocation = presentLocation.name,
            originLocation = originLocation.name,
            imageUrl = imageUrl
        )
    }
}


data class Location(
    val name: String,
    val url:String
)
