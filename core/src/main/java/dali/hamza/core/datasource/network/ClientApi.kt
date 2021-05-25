package dali.hamza.core.datasource.network

import dali.hamza.core.datasource.network.models.BaseListPokemonResponse
import dali.hamza.core.utilities.PokemonApiData
import dali.hamza.domain.models.Community
import dali.hamza.domain.models.Pokemon
import dali.hamza.domain.models.UserPokemon
import retrofit2.Response
import retrofit2.http.*

typealias CommunityJson = Map<String, List<UserPokemon>>?

interface ClientApi {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET
    suspend fun getListPokemonFomPokeApi(
        @Url url: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<BaseListPokemonResponse>

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET("activity")
    suspend fun getCommunityListPokemon(
        @Header("Authorization") authorization: String,
    ): Response<CommunityJson>


    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET("my-team")
    suspend fun getMyTeamListPokemon(
        @Header("Authorization") authorization: String,
    ): Response<List<Pokemon>>

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET("captured")
    suspend fun getCapturedListPokemon(
        @Header("Authorization") authorization: String,
    ): Response<List<Pokemon>>
}