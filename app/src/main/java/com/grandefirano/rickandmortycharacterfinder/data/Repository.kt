package com.grandefirano.rickandmortycharacterfinder.data

import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val apiSevice: ApiService) {


    suspend fun getAllCharacters():List<Character>{

       return apiSevice.getListOfCharacters().await().asDomainModel()
    }
    suspend fun getCharacter(userId:Int): Character {

        return apiSevice.getCharacter(userId).await().asDomainModel()
    }

    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            //Todo:get from rest and update Local SQL Database


        }
    }
}