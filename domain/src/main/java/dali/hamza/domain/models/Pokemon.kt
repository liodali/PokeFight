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
    val id: Int,
    val name: String,
    val url: String,
    val pokeGeoPoint: PokeGeoPoint
) : Parcelable