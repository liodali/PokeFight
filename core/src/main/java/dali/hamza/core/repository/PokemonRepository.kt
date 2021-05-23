package dali.hamza.core.repository

import dali.hamza.core.datasource.network.ClientApi
import dali.hamza.domain.models.*
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.Response
import javax.inject.Inject
import kotlin.random.Random

inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (isSuccessful) body()?.run(action)
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (PokeError) -> Unit) {
    if (!isSuccessful) errorBody()?.run { action(PokeError(message())) }
}

fun <T : Any> Response<T>.data(): MyResponse<T> {
    try {
        onSuccess {
            return MyResponse.SuccessResponse(it)
        }
        onFailure { return MyResponse.ErrorResponse(it) }
        return MyResponse.ErrorResponse(PokeError("GENERAL_NETWORK_ERROR"))
    } catch (e1: IOException) {
        return MyResponse.ErrorResponse(NetworkError("GENERAL_NETWORK_ERROR"))
    } catch (e: Exception) {
        return MyResponse.ErrorResponse(PokeError(e.fillInStackTrace().message))
    }

}


class PokemonRepository
@Inject constructor(
    private val api: ClientApi,

    ) : IPokemonRepository {


    override suspend fun getRandomPokemonLocationFlow(): Flow<IResponse> {
        val offset = Random(100).nextInt(0,5)
        val limit = Random(10).nextInt(10,60)
        return flow {
            val response = api.getListPokemonFomPokeApi(
                url="https://pokeapi.co/api/v2/pokemon",
                offset = offset,
                limit = limit
            ).data()
            emit(response)
        }
    }

    override suspend fun getAllFlow(filter: String): Flow<IResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFlow(): Flow<IResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommunityPokemonFlow(): Flow<IResponse> {
        TODO("Not yet implemented")
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