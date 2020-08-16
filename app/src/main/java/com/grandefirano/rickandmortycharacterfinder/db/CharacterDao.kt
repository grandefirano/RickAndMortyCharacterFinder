package com.grandefirano.rickandmortycharacterfinder.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grandefirano.rickandmortycharacterfinder.data.DomainCharacter

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters:List<DomainCharacter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: DomainCharacter)


    @Query("SELECT * FROM characters WHERE name LIKE :nameString AND status LIKE :statusString AND gender LIKE :genderString")
    fun getCharacters(nameString:String="%",
                      statusString: String="%",
                      genderString:String="%")
            :PagingSource<Int,DomainCharacter>

    @Query("DELETE FROM characters")
    suspend fun clearRepos()

}