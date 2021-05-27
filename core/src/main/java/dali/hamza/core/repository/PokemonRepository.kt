package dali.hamza.core.repository

import android.util.Log
import dali.hamza.core.datasource.db.daos.PokemonDao
import dali.hamza.core.datasource.db.entities.GeoPointEntity
import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.core.datasource.network.PokeApiClient
import dali.hamza.core.utilities.*
import dali.hamza.domain.models.*
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

inline fun <T> Response<T>.onSuccess(
    action: (T) -> Unit
): Response<T> {
    if (isSuccessful) body()?.run(action)
    return this
}

inline fun <T> Response<T>.onFailure(
    action: (PokeError) -> Unit
){
    if (!isSuccessful) {
        Log.e("error in request", "${this.raw().request.url}")
        Log.e("code request ${this.code()}", this.errorBody()?.string() ?: "error unknown")
         errorBody()?.run {
            action(PokeError(this.string()))
        }
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


class PokemonRepository @Inject constructor(
    var api: ClientApi,
    var pokeApiClient: PokeApiClient,
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
            val response = pokeApiClient.getListPokemonFomPokeApi(
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
            dao.getPokemonWithGeoPoint().collect { list ->
                when (list.isNotEmpty()) {
                    true -> emit(MyResponse.SuccessResponse(list.map {
                        it.toPokemonWithGeoPoint()
                    }))
                    false -> {
                        val token = sessionManager.getTokenFromDataStore.first()
                        val response = when (filter) {
                            "MyTeam" ->
                                api.getMyTeamListPokemon(
                                    authorization = "Bearer $token"
                                ).data { list ->
                                    list.map { p ->
                                        p.toPokemonWithGeoPoint()
                                    }
                                }
                            else ->
                                api.getCapturedListPokemon(
                                    authorization = "Bearer $token"
                                ).data { list ->
                                    list.map { p ->
                                        p.toPokemonWithGeoPoint()
                                    }
                                }
                        }
                        emit(response)

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

    override suspend fun insert(entity: PokemonWithGeoPoint): Flow<IResponse> {
        return flow {
            val token = sessionManager.getTokenFromDataStore.first()
            val response = api.addPokemonToMyTeam(
                "Bearer $token",
                entity.toMyPokemonTeamApi()
            ).data {
                it.values.first()
            }
            when (response.data!!) {
                true -> {
                    try {
                        withContext(IO) {
                            dao.insert(entity.pokemon.toPokemonDb())
                            dao.insertDetailPokemon(
                                GeoPointEntity(
                                    pokemonId = entity.pokemon.id,
                                    lon = entity.pokeGeoPoint.lon,
                                    lat = entity.pokeGeoPoint.lat
                                )
                            )
                        }
                        emit(MyResponse.SuccessResponse(data = true))
                    } catch (e: Exception) {
                        Log.e("error save in database", e.message!!)
                        emit(
                            MyResponse.ErrorResponse<Any>(
                                error =
                                PokeError("failed to capture the pokemon")
                            )
                        )
                    }
                }
                else -> {
                    emit(
                        MyResponse.ErrorResponse<Any>(
                            error =
                            PokeError("failed to capture the pokemon")
                        )
                    )
                }
            }
        }
    }


    override suspend fun getOneFlow(id: Int): Flow<IResponse> {
        return flow {
            val response = pokeApiClient.getDetailPokemonFomPokeApi(
                id = id
            ).data { apiData ->
                apiData.toDetailPokemon()
            }
            emit(response)
        }
    }

    override suspend fun insert(entity: Pokemon): Flow<IResponse> {
        throw Exception("Cannot use this method")
    }

    override suspend fun delete(entity: Pokemon) {
        throw Exception("Cannot use this method")
    }
}