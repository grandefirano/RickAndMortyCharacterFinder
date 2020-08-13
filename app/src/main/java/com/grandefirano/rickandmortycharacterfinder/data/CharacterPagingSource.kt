package com.grandefirano.rickandmortycharacterfinder.data

import androidx.paging.PagingSource
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import retrofit2.HttpException
import java.io.IOException

private const val CHARACTER_STARTING_PAGE_INDEX = 1


class CharacterPagingSource(
    private val service: ApiService,
    private val query: Search
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: CHARACTER_STARTING_PAGE_INDEX

        return try {

            val response = service.getListOfCharacters(
                name=query.name,
                gender = query.gender?.value,
                status = query.status?.value,
                page=page
            )
            val characters = response.asDomainModel()
            LoadResult.Page(
                data = characters,
                prevKey = if(page== CHARACTER_STARTING_PAGE_INDEX)null else page-1,
                nextKey = if(characters.isEmpty())null else page+1
            )
        }catch (exception:IOException){
            return LoadResult.Error(exception)
        }catch (exception:HttpException){
            return LoadResult.Error(exception)
        }
    }

}