package dali.hamza.domain.repository

import dali.hamza.domain.models.IResponse
import dali.hamza.domain.models.PokeGeoPoint
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.PokemonWithGeoPoint
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository : IBaseRepository<Pokemon> {

    suspend fun getRandomPokemonLocationFlow(
        userGeoPoint: PokeGeoPoint
    ): Flow<IResponse>

    suspend fun getAllFlow(filter: String): Flow<IResponse>

    suspend fun getCommunityPokemonFlow(): Flow<IResponse>

    suspend fun insert(entity: PokemonWithGeoPoint):Flow<IResponse>
}