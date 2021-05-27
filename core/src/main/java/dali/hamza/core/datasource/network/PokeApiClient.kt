package dali.hamza.core.datasource.network

import dali.hamza.core.datasource.network.models.BaseListPokemonResponse
import dali.hamza.core.datasource.network.models.PokeApiData
import retrofit2.Response
import retrofit2.http.*

interface PokeApiClient {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET("pokemon")
    suspend fun getListPokemonFomPokeApi(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<BaseListPokemonResponse>

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept:application/json",
    )
    @GET("pokemon/{id}")
    suspend fun getDetailPokemonFomPokeApi(
        @Path("id") id: Int,
    ): Response<PokeApiData>


}