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

//    val genderSearch:MutableLiveData<Search.GenderOption> =MutableLiveData()
//    val statusSearch:MutableLiveData<Search.StatusOption> = MutableLiveData()
//    val nameSearch:MutableLiveData<String> =MutableLiveData()

    val searchRequest:LiveData<Search>
    get() = _searchRequest
    private var _searchRequest: MutableLiveData<Search> = MutableLiveData()

    private var currentSearchResult: Flow<PagingData<Character>>?=null

    init {
        _searchRequest.value= Search()
    }

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

    fun onQueryTextChanged(newText: String) {
        val search=_searchRequest.value
        search?.name=newText
        _searchRequest.value=search
    }

    fun onQueryGenderChanged(gender: Search.GenderOption) {
        Log.d(TAG, "onQueryGenderChanged: gender changed for ${gender.value}")
        val search=_searchRequest.value
        search?.gender=gender
        _searchRequest.value=search
    }


}