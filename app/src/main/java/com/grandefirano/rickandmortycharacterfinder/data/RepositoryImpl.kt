package com.grandefirano.rickandmortycharacterfinder.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiSevice: ApiService) : IRepository {


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    override fun getCharactersSearchResult(query: Search): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(apiSevice, query) }
        ).flow
    }

}