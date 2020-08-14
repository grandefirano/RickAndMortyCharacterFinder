package com.grandefirano.rickandmortycharacterfinder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.grandefirano.rickandmortycharacterfinder.R


class Search{
    var name:String?=null

    var gender:GenderOption?=null

    var status:StatusOption?=null



   enum class GenderOption(val value:String?){
       ALL(null),
       MALE("male"),
       FEMALE("female"),
       GENDERLESS("genderless"),
       UNKNOWN("unknown")
    }

    enum class StatusOption(val value:String?){
        ALL(null),
        ALIVE("alive"),
        DEAD("dead"),
        UNKNOWN("unknown")
    }

}