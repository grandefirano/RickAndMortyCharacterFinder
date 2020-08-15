package com.grandefirano.rickandmortycharacterfinder.data

import android.util.Log
import androidx.paging.PagingSource
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

private const val CHARACTER_STARTING_PAGE_INDEX = 1


class CharacterPagingSource(
    private val service: ApiService,
    private val query: Search
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: CHARACTER_STARTING_PAGE_INDEX

        return try {
            Log.d("TAG", "load: 1")
            val response = service.getListOfCharacters(
                name=query.name,
                gender = query.gender?.value,
                status = query.status?.value,
                page=page
            )

            val characters = response?.asDomainModel()

            LoadResult.Page(
                data = characters,
                prevKey = if(page== CHARACTER_STARTING_PAGE_INDEX)null else page-1,
                nextKey = if(characters.isEmpty())null else page+1
            )
        }catch (exception:IOException){
            Log.i("TAG", "load: $exception")
            return LoadResult.Error(exception)
        }catch (exception:HttpException){
            Log.i("TAG", "load: $exception")
            when(exception.code()){
                404->{
                    LoadResult.Page(
                        data= listOf(),
                        prevKey = null,
                        nextKey = null
                    )
                }
                else-> return LoadResult.Error(exception)
            }
        }catch (exception:Exception){
            Log.i("TAG", "load: $exception")
            return LoadResult.Error(exception)

        }
    }

}