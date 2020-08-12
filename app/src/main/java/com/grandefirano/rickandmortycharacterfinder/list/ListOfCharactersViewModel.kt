package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.grandefirano.rickandmortycharacterfinder.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListOfCharactersViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    private val TAG = "ListOfCharactersViewMod"


    val listOfCharacters = liveData {
        emit(repository.getAllCharacters())
    }


    fun getListOfCharacters() {
        viewModelScope.launch {
            Log.d(TAG, "list ${repository.getAllCharacters()}")

            Log.d(TAG, "44 character ${repository.getCharacter(44)}")
        }
    }
}