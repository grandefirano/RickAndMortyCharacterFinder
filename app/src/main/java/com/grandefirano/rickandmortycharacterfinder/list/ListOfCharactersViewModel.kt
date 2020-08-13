package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.Repository
import kotlinx.coroutines.flow.Flow


class ListOfCharactersViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    private val TAG = "ListOfCharactersViewMod"


    private var currentQueryValue:String?=null
    private var currentSearchResult: Flow<PagingData<Character>>?=null

    fun searchCharacter(queryString:String):Flow<PagingData<Character>>{
        val lastResult=currentSearchResult
        if(queryString==currentQueryValue && lastResult!=null){
            return lastResult
        }
        currentQueryValue=queryString
        val newResult: Flow<PagingData<Character>> =repository.getCharactersSearchResult(queryString)
            .cachedIn(viewModelScope)
        Log.d(TAG, "searchCharacter: ")
        currentSearchResult=newResult
        return newResult
    }


}