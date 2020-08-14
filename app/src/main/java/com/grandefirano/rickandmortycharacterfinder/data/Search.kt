package com.grandefirano.rickandmortycharacterfinder.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.grandefirano.rickandmortycharacterfinder.R
import kotlinx.android.synthetic.main.layout_search.view.*

class Search{
    var name:String?=null

    var gender:GenderOption?=null

    var status:StatusOption?=null



   enum class GenderOption(val value:String){
       MALE("male"),
       FEMALE("female"),
       GENDERLESS("genderless"),
       UNKNOWN("unknown")
    }

    enum class StatusOption(val value:String){
        ALIVE("alive"),
        DEAD("dead"),
        UNKNOWN("unknown")
    }

}