package com.grandefirano.rickandmortycharacterfinder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.grandefirano.rickandmortycharacterfinder.R


class Search{
    var name:String?=null

    var gender:GenderOption=GenderOption.ALL

    var status:StatusOption=StatusOption.ALL



   enum class GenderOption(val value:String?){
       ALL(null),
       MALE("Male"),
       FEMALE("Female"),
       GENDERLESS("Genderless"),
       UNKNOWN("Unknown")
    }

    enum class StatusOption(val value:String?){
        ALL(null),
        ALIVE("Alive"),
        DEAD("Dead"),
        UNKNOWN("Unknown")
    }

}