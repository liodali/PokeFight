package dali.hamza.core.repository

import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.repository.IPokemonRepository
import kotlinx.coroutines.flow.Flow

class PokemonRepository :IPokemonRepository {
    override suspend fun getRandomPokemonLocationFlow(currentLocation: PokeGeoPoint): Flow<IResponse> {
        TODO("Not yet implemented")
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