package dali.hamza.core.datasource.network.models

import com.squareup.moshi.Json

data class MyPokemonTeamApi(
    val id: Int,
    val name: String,
    val captured_at: String,
    val captured_lat_at: Double,
    val captured_long_at: Double,
)

data class MyPokemonToSaveApi(
    @Json(name = "pokemon") val pokemon: Poke
)

data class Poke(
    val id: Int,
    val name: String,
    val lat: Double,
    val long: Double,
)
