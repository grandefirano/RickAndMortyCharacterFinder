package com.grandefirano.rickandmortycharacterfinder

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val apiSevice:ApiService) {


    suspend fun getCharacter(userId:String):List<Character>{

       return apiSevice.getCharacters().await().asDomainModel()
    }

    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            //Todo:get from rest and update Local SQL Database


        }
    }
}