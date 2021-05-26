package dali.hamza.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val url: String? = null,
    val captured_at: String = ""
) : Parcelable

@Parcelize
data class PokemonWithGeoPoint(
    val pokemon: Pokemon,
    val pokeGeoPoint: PokeGeoPoint
) : Parcelable

@Parcelize
data class DetailPokemon(
    val hp: Int,
    val types: List<String>,
    val moves: List<String>
) : Parcelable