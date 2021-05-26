package dali.hamza.core.datasource.network.models

import com.squareup.moshi.Json

data class PokeApiData(
    @Json(name = "id") val id: Int,
    @Json(name = "moves") val moves: List<Moves>,
    @Json(name = "stats") val stats: List<Stat>,
    @Json(name = "types") val types: List<Types>,
)

data class Stat(
    @Json(name = "base_stat") val base_stat: Int,
    @Json(name = "stat") val stat: StatInfo
)

data class StatInfo(
    @Json(name = "name") val name: String
)

data class Moves(
    @Json(name = "move") val move: MovePoke
)

data class MovePoke(
    @Json(name = "name") val name: String
)

data class Types(
    @Json(name = "type") val type: TypeInfo,
)

data class TypeInfo(
    @Json(name = "name") val name: String
)