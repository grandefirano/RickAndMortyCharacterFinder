package com.grandefirano.rickandmortycharacterfinder.data

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.grandefirano.rickandmortycharacterfinder.db.CharacterDatabase
import com.grandefirano.rickandmortycharacterfinder.db.RemoteKeys
import com.grandefirano.rickandmortycharacterfinder.network.ApiService
import com.grandefirano.rickandmortycharacterfinder.network.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val CHARACTER_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val query: Search,
    private val service: ApiService,
    private val characterDatabase: CharacterDatabase
) : RemoteMediator<Int, DomainCharacter>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, DomainCharacter>): MediatorResult {


        Log.d("TAG", "load: state pages ${state.pages}")
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: CHARACTER_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    }
                // If the previous key is null, then we can't request more data
                return MediatorResult.Success(endOfPaginationReached = true)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }

        }



        try {
            Log.d("TAG", "load: TRY")
            val apiResponse = service.getListOfCharacters(
                name = query.name,
                gender = query.gender?.value,
                status = query.status?.value,
                page = page
            )
            val characters = apiResponse.asDomainModel()
            Log.d("TAG", "load: characterS: $characters")
            val endOfPaginationReached = characters.isEmpty()
            characterDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    characterDatabase.remoteKeysDao().clearRemoteKeys()
                    characterDatabase.charactersDao().clearRepos()
                }
                val prevKey = if (page == CHARACTER_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = characters.map {
                    RemoteKeys(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                characterDatabase.remoteKeysDao().insertAll(keys)
                characterDatabase.charactersDao().insertAll(characters)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DomainCharacter>): RemoteKeys? {

        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DomainCharacter>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, DomainCharacter>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(characterId)
            }
        }
    }


}