package dali.hamza.core.repository

import dali.hamza.core.datasource.db.daos.PokemonDao
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.core.utilities.SessionManager
import dali.hamza.core.utilities.toPokemonWithGeoPoint
import dali.hamza.domain.models.*
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.Response
import javax.inject.Inject
import kotlin.random.Random

inline fun <T> Response<T>.onSuccess(
    action: (T) -> Unit
): Response<T> {
    if (isSuccessful) body()?.run(action)
    return this
}

inline fun <T> Response<T>.onFailure(
    action: (PokeError) -> Unit
) {
    if (!isSuccessful) errorBody()?.run {
        action(PokeError(this.string()))
    }
}

fun <T, R : Any> Response<T>.data(
    mapTo: (T) -> R
): MyResponse<R> {
    try {
        onSuccess {
            return MyResponse.SuccessResponse(mapTo(it))
        }
        onFailure {
            return MyResponse.ErrorResponse(it)
        }
        return MyResponse.ErrorResponse(
            error = PokeError("GENERAL_NETWORK_ERROR")
        )
    } catch (e1: IOException) {
        return MyResponse.ErrorResponse(
            error = NetworkError("GENERAL_NETWORK_ERROR")
        )
    } catch (e: Exception) {
        return MyResponse.ErrorResponse(
            error = PokeError(e.fillInStackTrace().message)
        )
    }

}


class PokemonRepository
@Inject constructor(
    var api: ClientApi,
    var dao: PokemonDao
) : IPokemonRepository {

    @Inject
    lateinit var sessionManager: SessionManager

    override suspend fun getRandomPokemonLocationFlow(
        userGeoPoint: PokeGeoPoint
    ): Flow<IResponse> {
        val offset = Random(100).nextInt(0, 5)
        val limit = Random(10).nextInt(10, 60)
        return flow {
            val response = api.getListPokemonFomPokeApi(
                url = "https://pokeapi.co/api/v2/pokemon/",
                offset = offset,
                limit = limit
            ).data {
                it.results.map { p ->
                    p.toPokemonWithGeoPoint(
                        userGeoPoint
                    )
                }.shuffled()
            }
            emit(response)
        }
    }

    override suspend fun getAllFlow(filter: String): Flow<IResponse> {
        return flow {
            when (filter) {
                "MyTeam" -> {
                    dao.getPokemonWithGeoPoint().collect { list ->
                        when (list.isNotEmpty()) {
                            true -> emit(MyResponse.SuccessResponse(list.map {
                                it.toPokemonWithGeoPoint()
                            }))
                            false -> {
                                val token = sessionManager.getTokenFromDataStore.first()
                                val response = api.getMyTeamListPokemon(
                                    authorization = "Bearer $token"
                                ).data { list->
                                    list.map { p->
                                        p.toPokemonWithGeoPoint()
                                    }
                                }
                                emit(response)
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun getAllFlow(): Flow<IResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommunityPokemonFlow(): Flow<IResponse> {
        return flow {
            val token = sessionManager.getTokenFromDataStore.first()
            val response = api.getCommunityListPokemon(
                authorization = "Bearer $token"
            ).data { json ->
                json!!.map { j ->
                    Community(
                        name = j.key,
                        listUserPokemon = j.value.map { pokemonU ->
                            pokemonU.copy(
                                typeCommunity = j.key
                            )
                        }
                    )
                }
            }
            emit(response)
        }
    }

    override suspend fun getOneFlow(id: Int): Flow<IResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(entity: Pokemon): Flow<IResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(entity: Pokemon) {
        TODO("Not yet implemented")
    }
}