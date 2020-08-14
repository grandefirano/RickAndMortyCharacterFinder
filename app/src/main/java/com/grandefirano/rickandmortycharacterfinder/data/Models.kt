package com.grandefirano.rickandmortycharacterfinder.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender:String,
    val originLocation:String,
    val presentLocation:String,
    val imageUrl:String
) : Parcelable