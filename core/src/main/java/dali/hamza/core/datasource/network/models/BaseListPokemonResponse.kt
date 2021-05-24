package dali.hamza.core.datasource.network.models

import com.squareup.moshi.Json
import dali.hamza.core.utilities.PokemonApiData

data class BaseListPokemonResponse(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<PokemonApiData>,
)