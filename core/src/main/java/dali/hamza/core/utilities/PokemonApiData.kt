package dali.hamza.core.utilities

import com.squareup.moshi.Json

data class PokemonApiData(
    @Json(name = "name")   val name:String,
    @Json(name = "url") val url:String
)
data class DetailPokemonApiData(
    val name:String,
    val url:String
)
