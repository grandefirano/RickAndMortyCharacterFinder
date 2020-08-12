package com.grandefirano.rickandmortycharacterfinder

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCharacterContainer(val results:List<NetworkCharacter>)

@JsonClass(generateAdapter = true)
data class NetworkCharacter(
    val id:Int,
    val name:String,
    val status:String,
    val species:String,
    val type:String,
    val gender:String,
    @Json(name="origin")
    val originLocation:Location,
    @Json(name="location")
    val presentLocation: Location,
    @Json(name="image")
    val imageUrl:String
)

fun NetworkCharacterContainer.asDomainModel():List<Character>{
    return results.map {
        Character(
            id = it.id,
            name = it.name,
            status = it.status,
            species = it.species,
            gender = it.gender,
            presentLocation = it.presentLocation.name,
            originLocation = it.originLocation.name,
            imageUrl = it.imageUrl
        )
    }
}


data class Location(
    val name: String,
    val url:String
)
