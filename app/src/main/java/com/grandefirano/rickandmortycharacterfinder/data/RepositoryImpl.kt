package com.grandefirano.rickandmortycharacterfinder.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.grandefirano.rickandmortycharacterfinder.db.CharacterDatabase
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiSevice: ApiService,
    private val database:CharacterDatabase
) : IRepository {


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    override fun getCharactersSearchResult(query:Search): Flow<PagingData<DomainCharacter>> {

        Log.d("TAG", "getCharactersSearchResult: ${query.name.formatToDBFormatQuery()}")
        Log.d("TAG", "getCharactersSearchResult: ${query.status.value.formatToDBFormatQuery()}")
        Log.d("TAG", "getCharactersSearchResult: ${query.gender.value.formatToDBFormatQuery()}")

        val dbQuery=query.apply {
            name.formatToDBFormatQuery()
            status.value.formatToDBFormatQuery()
            gender.value.formatToDBFormatQuery()
        }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
              query = query,
                service = apiSevice,
                characterDatabase = database
            ),
            //TODO:CHANGE TO PUT CHARACTERS HERE
            pagingSourceFactory = {database.charactersDao()
                .getCharacters()}
        ).flow
    }



    suspend fun getCharacter(userId:Int): DomainCharacter? {

        return apiSevice.getCharacter(userId)?.asDomainModel()
    }

    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            //Todo:get from rest and update Local SQL Database


        }
    }

    fun String.formatToDBFormatQuery():String{
        return "${this.replace(' ', '%')}"
    }
}