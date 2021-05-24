package dali.hamza.domain.models

data class Pokemon(
    val id :Int,
    val name:Int,
    val url:Int,
)


data class PokemonWithGeoPoint(
    val id :Int,
    val name:String,
    val url:String,
    val pokeGeoPoint: PokeGeoPoint
)