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
import com.grandefirano.rickandmortycharacterfinder.data.RepositoryImpl
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.shared.getViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class ListOfCharactersViewModel @ViewModelInject constructor(
    private val repository: RepositoryImpl,
    private val coroutineScopeProvider:CoroutineScope?=null
) : ViewModel() {
    private val TAG = "ListOfCharactersViewMod"

//    val genderSearch:MutableLiveData<Search.GenderOption> =MutableLiveData()
//    val statusSearch:MutableLiveData<Search.StatusOption> = MutableLiveData()
//    val nameSearch:MutableLiveData<String> =MutableLiveData()

    private val _navigateToCharacterDetail=MutableLiveData<Character>()
    val navigateToCharacterDetail:LiveData<Character>
    get() = _navigateToCharacterDetail

    fun onCharacterDetailNavigated(){
        _navigateToCharacterDetail.value=null
    }


    val searchRequest:LiveData<Search>
    get() = _searchRequest
    private val _searchRequest: MutableLiveData<Search> = MutableLiveData()

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
            .cachedIn(getViewModelScope(coroutineScopeProvider))
        Log.d(TAG, "searchCharacter: ")
        currentSearchResult=newResult
        return newResult
    }

    fun onQueryTextChanged(newText: String) {
        val search=_searchRequest.value
        search?.name=newText
        _searchRequest.value=search
    }

    fun onQueryGenderChanged(gender: Search.GenderOption?) {
        Log.d(TAG, "onQueryGenderChanged: gender changed for ${gender?.value}")
        val search=_searchRequest.value
        search?.gender=gender
        _searchRequest.value=search
    }
    fun onQueryStatusChanged(status:Search.StatusOption?) {
        Log.d(TAG, "onQueryGenderChanged: gender changed for ${status?.value}")
        val search=_searchRequest.value
        search?.status=status
        _searchRequest.value=search
    }

    fun onCharacterClicked(character: Character) {
        _navigateToCharacterDetail.value=character
    }


}