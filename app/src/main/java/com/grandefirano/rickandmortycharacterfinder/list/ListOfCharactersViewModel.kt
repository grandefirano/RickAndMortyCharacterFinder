package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.Repository
import com.grandefirano.rickandmortycharacterfinder.data.Search
import kotlinx.coroutines.flow.Flow


class ListOfCharactersViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    private val TAG = "ListOfCharactersViewMod"


    private var searchRequest: LiveData<Search> = MutableLiveData()
    private var currentSearchResult: Flow<PagingData<Character>>?=null

    fun searchCharacters(search:Search):Flow<PagingData<Character>>{
       // val lastResult=currentSearchResult
        /*if(queryString==currentSearc&& lastResult!=null){
            return lastResult
        }*/
        //searchRequest.=queryString


        val newResult: Flow<PagingData<Character>> =repository.getCharactersSearchResult(search)
            .cachedIn(viewModelScope)
        Log.d(TAG, "searchCharacter: ")
        currentSearchResult=newResult
        return newResult
    }


}