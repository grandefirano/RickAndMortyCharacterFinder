package com.grandefirano.rickandmortycharacterfinder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val apiSevice: ApiService) {


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    fun getCharactersSearchResult(query:Search): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {CharacterPagingSource(apiSevice,query)}
        ).flow
    }


    suspend fun getCharacter(userId:Int): Character {

        return apiSevice.getCharacter(userId).asDomainModel()
    }

    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            //Todo:get from rest and update Local SQL Database


        }
    }
}