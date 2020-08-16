package com.grandefirano.rickandmortycharacterfinder.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
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
) : RemoteMediator<Int, Character>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys=getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1)?: CHARACTER_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")

                remoteKeys.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )


            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        try {
            val apiResponse = service.getListOfCharacters(
                name = query.name,
                gender = query.gender?.value,
                status = query.status?.value,
                page = page
            )
            val characters = apiResponse.asDomainModel()
            val endOfPaginationReached = characters.isEmpty()
            characterDatabase.withTransaction {

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
            return MediatorResult.Success(endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Character>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)

            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Character>): RemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(character.id)

            }

    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Character>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                characterDatabase.remoteKeysDao().remoteKeysCharacterId(characterId)
            }
        }
    }
}