package com.grandefirano.rickandmortycharacterfinder.data

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender:String,
    val originLocation:String,
    val presentLocation:String,
    val imageUrl:String
)