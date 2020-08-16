package com.grandefirano.rickandmortycharacterfinder.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.data.Search

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters:List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: Character)

    @Query("SELECT * FROM characters" +
            "WHERE name LIKE :nameString AND status LIKE :statusString" +
            " AND gender LIKE :genderString ORDER BY id")
    fun getCharacters(nameString:String="%",
                      statusString: String="%",
                      genderString:String="%")
            :PagingSource<Int,Character>

    @Query("DELETE FROM characters")
    suspend fun clearRepos()

}