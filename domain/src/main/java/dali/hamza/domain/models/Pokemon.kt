package dali.hamza.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Pokemon(
    val id: Int,
    val name: Int,
    val url: Int,
    val capturedDate: String? = null
)

@Parcelize
data class PokemonWithGeoPoint(
    val id: Int,
    val name: String,
    val url: String,
    val pokeGeoPoint: PokeGeoPoint
) : Parcelable