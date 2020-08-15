package com.grandefirano.rickandmortycharacterfinder.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IRepository{

    fun getCharactersSearchResult(query:Search): Flow<PagingData<Character>>
}