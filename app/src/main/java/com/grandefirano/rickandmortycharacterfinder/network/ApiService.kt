package com.grandefirano.rickandmortycharacterfinder.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

import com.grandefirano.rickandmortycharacterfinder.data.DomainCharacter
import com.grandefirano.rickandmortycharacterfinder.data.Search
import com.grandefirano.rickandmortycharacterfinder.db.CharacterDatabase
import com.grandefirano.rickandmortycharacterfinder.db.RemoteKeys
import kotlinx.coroutines.Deferred
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.lang.Exception

const val GET_LIST_OF_CHARACTERS: String = "character"
const val GET_CHARACTER:String="character/{id}"

interface ApiService {
    @GET(GET_LIST_OF_CHARACTERS)
    suspend fun getListOfCharacters(
        @Query("name") name:String?=null,
        @Query("gender") gender:String?=null,
        @Query("status") status:String?=null,
        @Query("page") page:Int=1
    ):NetworkCharactersContainer

    @GET(GET_CHARACTER)
    suspend fun getCharacter(@Path("id")id:Int):NetworkCharacter
}


//@OptIn(ExperimentalPagingApi::class)
//class CharacterRemoteMediator(
//    private val query: Search,
//    private val service: ApiService,
//    private val characterDatabase: CharacterDatabase
//) : RemoteMediator<Int, DomainCharacter>() {
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, DomainCharacter>
//    ): MediatorResult {
//        Log.d("TAG", "load: database $characterDatabase")
//        Log.d("TAG", "load:state ${state.pages} ")
//
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                Log.d("TAG", "load: REFRESH")
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                Log.d("TAG", "load: remoteKeys $remoteKeys")
//                Log.d("TAG", "load: remoteKeys ${remoteKeys?.nextKey?.minus(1)}")
//                remoteKeys?.nextKey?.minus(1) ?: CHARACTER_STARTING_PAGE_INDEX
//            }
//            LoadType.PREPEND -> {
////                Log.d("TAG", "load: PREPEND")
////                val remoteKeys = getRemoteKeyForFirstItem(state)
////
////                remoteKeys?.prevKey ?: return MediatorResult.Success(
////                    endOfPaginationReached = true
////                )
//                1
//
//            }
//            LoadType.APPEND -> {
//                Log.d("TAG", "load: APPEND")
//                val remoteKeys = getRemoteKeyForLastItem(state) ?: 1
////                if (remoteKeys?.nextKey == null)
////                    return MediatorResult.Success(
////                        endOfPaginationReached = true
////                    )
//                1
//            }
//        }
//        try {
//            Log.d("TAG", "load: TRY")
//            val apiResponse = service.getListOfCharacters(
//                name = query.name,
//                gender = query.gender?.value,
//                status = query.status?.value,
//                page = page
//            )
//            val characters = apiResponse.asDomainModel()
//            Log.d("TAG", "load: characterS: $characters")
//            val endOfPaginationReached = characters.isEmpty()
//            characterDatabase.withTransaction {
//
//                if (loadType == LoadType.REFRESH) {
//                    characterDatabase.remoteKeysDao().clearRemoteKeys()
//                    characterDatabase.charactersDao().clearRepos()
//                    Log.d("TAG", "load: type REFRESH")
//                }
//                val prevKey = if (page == CHARACTER_STARTING_PAGE_INDEX) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                Log.d("TAG", "load: prevkey $prevKey")
//                Log.d("TAG", "load: nextKEy $nextKey")
//
//                val keys = characters.map {
//                    RemoteKeys(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
//                }
//                Log.d("TAG", "load: keys $keys")
//                characterDatabase.remoteKeysDao().insertAll(keys)
//                characterDatabase.charactersDao().insertAll(characters)
//                Log.d(
//                    "TAG",
//                    "TOOOO CHARACTER: ${characterDatabase.charactersDao().getCharacters()}"
//                )
//                Log.d(
//                    "TAG",
//                    "TOWAZNE:${characterDatabase.remoteKeysDao()
//                        .remoteKeysCharacterId(characters.lastIndex)} "
//                )
//                Log.d("TAG", "lend of pagination $endOfPaginationReached ")
//
//            }
//            return MediatorResult.Success(endOfPaginationReached)
//        } catch (exception: IOException) {
//            Log.i("TAG", "Error: $exception")
//
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            Log.i("TAG", "Error: $exception")
//
//            when (exception.code()) {
//                404 -> MediatorResult.Success(true)
//            }
//            return MediatorResult.Error(exception)
//        } catch (exception: Exception) {
//            Log.i("TAG", "load: ${exception.cause} ${exception.localizedMessage}")
//            return MediatorResult.Error(exception)
//
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DomainCharacter>): RemoteKeys? {
//        Log.d("TAG", "getRemoteKeyForLastItem: ${state.pages} ")
//        Log.d("TAG", "getRemoteKeyForLastItem: ${state.pages.firstOrNull()} ")
//
//        return state.pages.lastOrNull()
//            ?.data?.lastOrNull()
//            ?.let { character ->
//                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)
//
//            }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DomainCharacter>): RemoteKeys? {
//        Log.d("TAG", "getRemoteKeyForFirstItem: ${state.pages} ")
//        Log.d("TAG", "getRemoteKeyForFirstItem: ${state.pages.firstOrNull()} ")
//
//        return state.firstItemOrNull()?.let {
//            characterDatabase.remoteKeysDao().remoteKeysCharacterId(it.id)
//        }
//
//        return state.pages.firstOrNull() { it.data.isNotEmpty() }
//            ?.data?.firstOrNull()
//            ?.let { character ->
//                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)
//
//            }
//
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(
//        state: PagingState<Int, DomainCharacter>
//    ): RemoteKeys? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { characterId ->
//                characterDatabase.remoteKeysDao().remoteKeysCharacterId(characterId)
//            }
//        }
//    }
//}
