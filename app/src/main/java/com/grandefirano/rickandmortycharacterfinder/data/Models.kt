package com.grandefirano.rickandmortycharacterfinder.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "characters")
data class DomainCharacter (
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val name:String,
    val status:String,
    val species:String,
    val gender:String,
    @ColumnInfo(name = "origin_location")
    val originLocation:String,
    @ColumnInfo(name="present_location")
    val presentLocation: String,
    @ColumnInfo(name="image_url")
    val imageUrl: String

):Parcelable
